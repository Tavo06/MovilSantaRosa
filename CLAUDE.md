# MovilSantaRosa — Project Guide for Claude Code

This file is the entry point for working on this codebase. It is factual, based on the
actual code as of this writing — nothing here is aspirational or invented. Deeper,
topic-specific instructions live in `.claude/skills/project-*/SKILL.md`; load the
relevant one before doing non-trivial work in that area.

## 1. What this app is

An Android app (Kotlin + Jetpack Compose) for managing a members' association ("socios"):
member roster, fines ("multas"), payments ("pagos"), and announcements/posts ("anuncios",
with a map location). Two roles: `ADMIN` (can create/edit fines, payments, posts) and
`PARTNER` (read-only member view). Package root: `com.upsjb.movilsantarosa`. Application ID
`com.upsjb.movilsantarosa`. Single Gradle module: `app`.

## 2. Versions (from `gradle/libs.versions.toml` and `app/build.gradle.kts`)

- Kotlin 2.2.10, AGP 9.2.1, KSP 2.3.4
- compileSdk / targetSdk 37, minSdk 26, JVM target 17
- Compose BOM `2026.02.01`, Material 3 (no Material 2 anywhere)
- Navigation **3** (`androidx.navigation3`, `1.2.0-alpha05`) — not Navigation Compose 2.x
- Hilt 2.59.2 (+ `hilt-navigation-compose`)
- Firebase BOM `34.15.0`: `firebase-auth`, `firebase-database` (Realtime Database). No
  Firestore, no Storage.
- MapLibre Compose `0.13.0` (`org.maplibre.compose`) — not Google Maps Compose. Google
  `play-services-location` is used only for device GPS (`FusedLocationProviderClient`).
- Accompanist Permissions `0.37.3`
- kotlinx.serialization (models + Nav3 `NavKey` routes)
- **Declared but unused anywhere in source**: Room (`androidx.room`), Ktor client. Do not
  assume either is wired up — see `project-debugging` skill.

## 3. Architecture in one paragraph

Feature-based, not a single global Clean Architecture module. Each feature under
`app/src/main/java/com/upsjb/movilsantarosa/feature/<name>/` has its own
`data/{model,repository}`, `domain/{model,repository,usecase}`, `di`, and
`ui/<screen>/{components}`. Data flows one way:
**Firebase (Realtime DB / Auth) → `*RepositoryImpl` → `*UseCase` → `*ViewModel`
(`StateFlow`) → `@Composable Screen`**. `core/` holds what's shared across features:
navigation, reusable UI components, theme, permissions, date/string utils. See
`project-architecture` skill for the full package map and where to put new files.

Features present: `auth`, `home`, `members`, `fine`, `payments`, `post`. `home` is a
read-only dashboard that aggregates data from the other four features' use cases — it has
no Firebase access of its own (its `HomeRepository` interface exists but is unused/unbound,
see Known Issues).

## 4. Where things live (quick map)

| Concern | Path |
|---|---|
| Entry point | `MainActivity.kt`, `MovilApp.kt` (`@HiltAndroidApp`) |
| Session/root nav decision | `MainViewModel.kt` (`SessionState`) |
| Nav graphs | `core/navigation/{AppNavHost,AuthNavHost,MainNavHost}.kt` |
| Nav routes | `core/navigation/component/Destinations.kt` |
| Nav plumbing (Nav3 wrapper) | `core/navigation/component/{Navigator,NavigationState,BottomSheetSceneStrategy}.kt` |
| Cross-screen results | `core/navigation/results/NavigationResults.kt` |
| Shared Compose components | `core/uicomponents/*.kt` |
| Theme / colors / typography | `core/theme/{Theme.kt,Type.kt}` |
| Location permission helper | `core/permission/LocationPermission.kt` |
| Date/number/string helpers | `core/utils/{DateUtil.kt,StringUtil.kt}` |
| Per-feature code | `feature/<name>/{data,domain,di,ui}` |

## 5. Data flow, concretely

1. `*RepositoryImpl` (in `feature/<name>/data/repository/`) talks to `FirebaseDatabase` /
   `FirebaseAuth` directly. Reads that need to stay live use
   `callbackFlow { ValueEventListener }`; one-shot reads/writes use `.get().await()` /
   `.setValue().await()` wrapped in `try/catch` returning Kotlin `Result<T>`.
2. `*UseCase` classes (in `domain/usecase/`) are single-operation, `@Inject`-constructed,
   `operator fun invoke(...)`. Business rules (e.g. "only `ADMIN` may register a fine") live
   here, not in the ViewModel or repository — see `RegisterFineUseCase`,
   `RegisterPaymentUseCase`.
3. `*ViewModel` (`@HiltViewModel`) exposes a single `StateFlow<XUiState>` (list screens) or
   `StateFlow<XFormUiState>` (form screens), built with `combine(...).stateIn(viewModelScope,
   SharingStarted.WhileSubscribed(5000), ...)` for list screens.
4. `@Composable XScreen` collects via `collectAsStateWithLifecycle()` and does a `when` over
   the sealed `UiState`.

Full detail and copy-paste-ready patterns: `project-architecture`, `project-firebase`,
`project-models-repositories` skills.

## 6. UI conventions

Material 3 only. Every screen reuses `core/uicomponents/*` for search bars, form fields,
dropdowns, date pickers, buttons, dialogs, skeleton/empty/error states — **never build a new
one-off button/dialog/text field; reuse or extend an existing component.** Full inventory in
`project-ui-system` skill.

## 7. Navigation

Custom-built on Navigation 3 (`Navigator` + `NavigationState` + per-top-level-route back
stacks + a hand-rolled `BottomSheetSceneStrategy` for modal bottom sheets). Routes are
`@Serializable` `NavKey` objects/classes in `Destinations.kt`. Screen-to-screen results (e.g.
picking a member) go through `LocalResultEventBus` / `ResultEffect<T>`, not through
ViewModel shared state. See `project-navigation` skill before adding a screen.

## 8. Firebase structure

Realtime Database root nodes (string constants defined next to their repository):
`user_database` (used by both `AuthRepositoryImpl` and `MemberRepositoryImpl` — a user
record doubles as a member record), `fine_database`, plus equivalent `*_database` nodes for
payments and posts (see each `*RepositoryImpl`). No Firestore. Auth is email/password via
`FirebaseAuth`; role (`ADMIN`/`PARTNER`) is stored as a field under `user_database/<uid>` and
read back after login. **No Firebase security rules file exists in this repo** — role checks
are enforced only client-side in use cases. See `project-firebase` skill.

## 9. Map / location

`feature/post/ui/location_picker` is the only map screen: MapLibre (`MaplibreMap`,
`org.maplibre.compose`) with the OpenFreeMap `liberty` style, a fixed center pin (not a
draggable marker), custom `MapControls` FAB stack, permission requested via
`core/permission/LocationPermission.kt`, initial position via
`GetCurrentLocationUseCase` → `FusedLocationProviderClient`, reverse geocoding via
`GetAddressUseCase` → Android `Geocoder` (not a network API despite Ktor being a
dependency).

## 10. Known issues (documented, not fixed — see full report for file/line detail)

- **HIGH**: `LocationPickerViewModel.confirmLocation()` is invoked twice per tap in
  `MapControls`'s `onSelect` callback (`LocationPickerScreen.kt`) — a stale-location call and
  a real async one race against the same `selectedLocation` `StateFlow`.
- **MEDIUM**: `Fine.kt` (domain model, `feature/fine/domain/model/Fine.kt`) imports
  `FineFormState` from `feature/fine/ui/fine_form` — domain depends on UI, backwards from
  the intended direction.
- **MEDIUM**: `FineUiState.Success.filteredFines` and `PaymentUiState.Success.filteredPayments`
  getters are dead code — filtering already happens in the ViewModel's `combine`. Don't copy
  this pattern for new list screens.
- **MEDIUM**: List-screen repositories (`FineRepositoryImpl.getAllFines`, etc.) never
  surface `onCancelled`/exceptions as a UI `Error` state — a Firebase listener failure just
  silently stops updating the list.
- **LOW**: Room and Ktor client are declared Gradle dependencies with zero usages in
  `app/src/main/java`. Don't add code assuming either is wired up (no `RoomDatabase`, no
  `HttpClient` provider exists).
- **LOW**: `USER_DATABASE = "user_database"` is duplicated as a literal in both
  `AuthRepositoryImpl.kt` and `MemberRepositoryImpl.kt`.
- **LOW**: `home/domain/repository/HomeRepository.kt` has no implementation and no Hilt
  binding — it's unused; `GetHomeStatsUseCase` actually composes `GetAllMembersUseCase` /
  `GetFinesUseCase` / `GetAllPostsUseCase` instead.
- **MEDIUM**: `RegisterPostUseCase`/`UpdatePostUseCase` (`feature/post/domain/usecase/`) do
  not check `UserRole.ADMIN` via `CurrentUserUseCase`, unlike the equivalent fine and
  payment use cases — any logged-in user (including `PARTNER`) can currently create/edit
  posts. Inconsistent with the rest of the app's admin-only-writes convention; confirm with
  the user whether this is intentional before "fixing" it.
- **Needs manual verification**: `app/google-services.json` is committed to git (a
  commented-out `.gitignore` entry shows this was a deliberate choice, not an oversight).
  Confirm this is intended before touching `.gitignore` or Firebase config.

## 11. Build / test / debug commands

```
./gradlew assembleDebug        # build debug APK
./gradlew test                 # unit tests (app/src/test)
./gradlew connectedAndroidTest # instrumented tests (app/src/androidTest)
./gradlew lint
```
On Windows use `gradlew.bat` instead of `./gradlew`. There is no CI config in this repo to
mirror.

## 12. Rules for modifying this project

Full checklist in `project-safe-modification` skill; the essentials:

- Read the existing feature that most resembles what you're building (usually `fine` or
  `payments` — they're the most complete pair: list + form + picker) and mirror its
  `data/domain/di/ui` split and naming before writing new code.
- Reuse `core/uicomponents/*` instead of inlining new UI primitives.
- Put business rules (role checks, validation) in a `UseCase`, not the ViewModel or
  Composable.
- Don't add Room or Ktor code without confirming with the user first — both are unused
  dependencies today; adding usage is a real architectural decision, not a given.
- Don't "fix" the Known Issues above unless asked — they're documented, not queued.
- After any non-trivial change, run `./gradlew assembleDebug` (or `test`) before calling the
  task done.
