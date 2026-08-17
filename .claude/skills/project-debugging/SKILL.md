---
name: project-debugging
description: MovilSantaRosa debugging guide - where to look first for Gradle, Compose, Navigation 3, Firebase, Hilt, and map/location errors in this specific codebase. Use when investigating a build failure, crash, or unexpected runtime behavior.
---

# MovilSantaRosa — Debugging Guide

Start by identifying the category, then jump to the matching section. Build/run with
`./gradlew assembleDebug` (or `gradlew.bat` on Windows) before/after investigating to get a
clean error signal.

## Gradle / dependency errors

- Single module (`app`), version catalog at `gradle/libs.versions.toml` — always add/bump
  versions there, never inline a version string in `app/build.gradle.kts`.
- Plugins applied: `com.android.application`, `org.jetbrains.kotlin.plugin.compose`,
  `com.google.gms.google-services`, `com.google.devtools.ksp`,
  `com.google.dagger.hilt.android`, `org.jetbrains.kotlin.plugin.serialization`. A missing
  plugin application is a common cause of "unresolved reference" for generated code (Hilt
  `_HiltComponents`, KSP-generated Room/Hilt classes) — check `app/build.gradle.kts`'s
  `plugins { }` block first.
- **Room and Ktor are declared dependencies with zero usages in the source tree.** If you see
  an error referencing `RoomDatabase`, `@Entity`, `Dao`, or `HttpClient` and can't find the
  class, it's because no such class exists yet in this codebase — don't assume there's a
  hidden Room/Ktor setup you're missing; either it needs to be built from scratch or the
  reference is a mistake.
- `compileSdk`/`targetSdk` 37, `minSdk` 26, JVM target 17 — a "class file has wrong version"
  or Java-version mismatch error usually means a tool/JDK mismatch with `VERSION_17`, not a
  code bug.

## Compose compiler / recomposition errors

- Compose compiler comes from `org.jetbrains.kotlin.plugin.compose` (K2 Compose compiler,
  bundled with Kotlin 2.2.10) — no separate `composeOptions { kotlinCompilerExtensionVersion
  }` block exists or is needed.
- Compose BOM `2026.02.01` pins all `androidx.compose.*` artifact versions — if a Compose
  API doesn't resolve, check the BOM version's release notes rather than trying to bump an
  individual `androidx.compose.*` library version (they're BOM-managed, not
  individually versioned in `libs.versions.toml` for most modules).
- Unexpected recomposition/state-loss: check whether the offending state is a bare
  `remember { mutableStateOf(...) }` that should be `rememberSaveable`, or state that should
  live in the ViewModel's `StateFlow` instead (see `project-compose-kotlin` skill). Nav3's
  `rememberSaveableStateHolderNavEntryDecorator`/`rememberViewModelStoreNavEntryDecorator`
  (wired in `NavigationState.toDecoratedEntries`) are what make per-tab state and ViewModels
  survive tab switches — if state resets unexpectedly when switching bottom-nav tabs, check
  that the new screen's `entry<...>` is registered under the correct top-level route's back
  stack, since only `startRoute` and the current `topLevelRoute` are kept "in use" by
  `NavigationState.getTopLevelRoutesInUse()`.

## Navigation errors

- `error("Stack for ... not found")` from `Navigator.goBack()` means `state.backStacks`
  doesn't have an entry for the current `topLevelRoute` — this happens if a route was added
  to `Destinations.kt`/used in `navigate()` without being included in the `topLevelRoutes`
  set passed to `rememberNavigationState` (for a top-level route) or if it's being treated as
  a top-level route when it should be a pushed route.
- A screen that never appears after `navigator.navigate(SomeDestination)`: check it has a
  matching `entry<SomeDestination> { ... }` in the right `NavHost` (`AuthNavHost` vs
  `MainNavHost`) — there's no runtime warning for a missing entry, `NavDisplay` will just
  render nothing for that key.
- A bottom sheet not appearing as a sheet (renders as a normal full screen instead): the
  `entry<...>(metadata = BottomSheetSceneStrategy.bottomSheet())` argument is missing, or
  `bottomSheetStrategy` wasn't included in `NavDisplay`'s `sceneStrategies` list in
  `MainNavHost.kt`.
- A picker result (`ResultEffect<T>`) never arriving at the origin screen: confirm the type
  parameter `T` matches exactly between `resultBus.sendResult(result = x)` (picker side) and
  `ResultEffect<T> { }` (origin side) — see `project-navigation` skill.

## Firebase errors

- `getValue(XModel::class.java)` returning `null` / throwing: the stored JSON shape doesn't
  match `XModel`'s fields, or a field's default doesn't match the Kotlin no-arg-constructor
  requirement (every `XModel` property must have a default value). Check the actual node
  content against `XModel`'s field names/types.
- A list screen silently stops updating instead of showing an error: Realtime Database
  listeners call `close(error.toException())` on `onCancelled`, but list ViewModels
  (`FineViewModel`, etc.) don't currently turn that into `XUiState.Error` — this is a known
  gap (see `project-firebase` skill), not a new bug you introduced. Add error handling
  explicitly if asked to fix it; don't assume it already works.
- `"No hay sesión activa"` / login loops: check `MainViewModel.refreshSession()` and
  `CurrentUserUseCase` → `AuthRepositoryImpl.getCurrentUser()` — it fails if
  `FirebaseAuth.currentUser` is null OR if `user_database/<uid>` has no snapshot, so a valid
  Auth account with no matching database record will still appear logged out from the app's
  perspective.
- Permission-denied-style Firebase errors: this repo has **no visible security rules file**,
  so a rules issue can't be diagnosed from the codebase — check the Firebase console rules
  configuration directly, and say so rather than guessing.

## Hilt / DI errors

- `[Hilt] Cannot be provided without an @Inject constructor` or similar for a repository
  interface: check the feature's `di/XModule.kt` has a `@Binds` (or `@Provides`) method for
  it, and that the module has `@InstallIn(SingletonComponent::class)`.
- A field/class expecting Hilt injection but crashing: confirm `MainActivity` has
  `@AndroidEntryPoint` and `MovilApp` has `@HiltAndroidApp` — both are required and both
  already exist; if either is missing after an edit, that's the regression.
- Two modules provided the same type (duplicate binding): check you haven't defined a second
  `@Provides`/`@Binds` for `FirebaseAuth`/`FirebaseDatabase` outside `LoginModule.kt` — those
  two are provided exactly once, there.

## Runtime crashes in the map / location picker

- `MapControls`'s `onSelect` currently calls `LocationPickerViewModel.confirmLocation()`
  **twice** in one tap (a known issue, see `CLAUDE.md` Known Issues) — if you're chasing a
  double-navigation-back or a race between two geocode results in
  `LocationPickerScreen.kt`/`LocationPickerViewModel.kt`, this is almost certainly why. It's
  documented, not something you introduced.
- `MissingPermission` lint suppressions exist in `LocationRepositoryImpl` — the actual
  runtime permission gate is `core/permission/LocationPermission.kt`
  (`ACCESS_FINE_LOCATION`), invoked before `GetCurrentLocationUseCase` is called. A crash
  here usually means a screen calls the location use case without first going through
  `LocationPermission`.

## State / serialization problems

- A `NavKey` route failing to serialize/restore across process death: every route in
  `Destinations.kt` must stay `@Serializable` with only serializable parameter types
  (`String?`, primitives) — don't add a non-serializable parameter to a route.
- A model failing to round-trip through Firebase: check every field in the `XModel` has a
  default (see Firebase section above) and that enum values match `FineReason`/`FineStatus`/
  `UserRole`-style enums exactly (Firebase stores/reads the Kotlin enum constant name as a
  string).
