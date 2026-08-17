---
name: project-architecture
description: MovilSantaRosa project structure, layering, and where to place new files. Use before creating any new file, feature, or screen in this Android app.
---

# MovilSantaRosa — Architecture

Read `CLAUDE.md` at the repo root first for the high-level summary. This skill goes deeper
on package layout and placement rules.

## Module layout

Single Gradle module: `app` (`com.upsjb.movilsantarosa`, namespace and applicationId both
`com.upsjb.movilsantarosa`). No `core`/`data`/`domain` Gradle modules — everything is one
module, split by Kotlin package.

## Package tree

```
app/src/main/java/com/upsjb/movilsantarosa/
├── MainActivity.kt              # @AndroidEntryPoint, sets content to AppNavHost inside MovilSantaRosaTheme
├── MovilApp.kt                  # @HiltAndroidApp Application class
├── MainViewModel.kt             # SessionState (Loading/LoggedOut/LoggedIn(role)) — decides Auth vs Main nav graph
├── core/
│   ├── contact/                 # ContactLauncher.kt — launches phone/WhatsApp intents for member contact
│   ├── navigation/               # Nav3 graphs + custom Navigator/NavigationState (see project-navigation skill)
│   ├── permission/               # LocationPermission.kt — Accompanist wrapper composable
│   ├── theme/                    # Theme.kt (MovilSantaRosaTheme, color schemes), Type.kt (Typography)
│   ├── uicomponents/             # Shared Compose components (see project-ui-system skill)
│   └── utils/                    # DateUtil.kt, StringUtil.kt — small top-level extension functions
└── feature/
    ├── auth/       # login, register, session (User, UserRole)
    ├── home/       # dashboard aggregating stats from other features (no own Firebase access)
    ├── members/    # member roster + member picker bottom sheet
    ├── fine/       # fines: list, form, picker bottom sheet
    ├── payments/   # payments: list, form
    └── post/       # announcements: list, form, location picker (MapLibre)
```

## Per-feature package shape (the pattern to copy)

Every feature under `feature/<name>/` follows this exact shape. `fine` and `payments` are
the most complete examples (list + form + cross-feature picker) — copy from them, not from
`home` (which is a special aggregator with no data layer) or `auth` (special-cased, no
`ui/<name>/components` split).

```
feature/<name>/
├── data/
│   ├── model/          # <Name>Model — @Serializable, flat, all-defaults data class (Firebase DTO shape)
│   └── repository/     # <Name>RepositoryImpl — talks to FirebaseDatabase/FirebaseAuth directly
├── domain/
│   ├── model/           # <Name> — the app-facing domain model, plus toDomain()/toModel() mappers
│   │                     # in the SAME file as the domain model (see project-models-repositories)
│   ├── repository/      # <Name>Repository — interface only, implemented in data/repository
│   ├── usecase/         # One class per operation, @Inject constructor, `operator fun invoke(...)`
│   └── request/         # (auth only) request DTOs like RegisterRequest, for multi-field operations
├── di/
│   └── <Name>Module.kt  # Hilt @Module @InstallIn(SingletonComponent::class); @Binds repo impl→interface
└── ui/
    ├── <name>/                    # main list screen: <Name>Screen.kt, <Name>UiState.kt, <Name>ViewModel.kt
    │   └── components/            # <Name>Item.kt, <Name>List.kt, <Name>Description.kt
    ├── <name>_form/                # create/edit screen: <Name>FormScreen.kt, <Name>FormUiState.kt (+ FormState,
    │   └── component/               # ActionState, Mode nested in the UiState file), <Name>FormViewModel.kt
    │                                 # component/: <Name>FormContent.kt (the form body), <Name>FormActionHandler.kt
    │                                 # (LaunchedEffect that reacts to ActionState.Success/Error)
    └── <name>_picker/               # (fine, members only) bottom-sheet picker used by other features' forms
```

## Where to put a new feature

1. Create `feature/<newname>/{data/{model,repository},domain/{model,repository,usecase},di,ui/<newname>/components}`.
2. Model the Firebase DTO in `data/model/<Name>Model.kt` — `@Serializable data class`, every
   field defaulted (Firebase deserializes with `getValue(Model::class.java)`, which requires
   a no-arg constructor via defaults).
3. Model the domain type in `domain/model/<Name>.kt`, with `fun <Name>Model.toDomain()` and
   `fun <Name>.toModel()` in the same file.
4. Define `<Name>Repository` interface in `domain/repository/`, implement in
   `data/repository/<Name>RepositoryImpl.kt`, bind with `@Binds` in `di/<Name>Module.kt`.
5. Add use cases in `domain/usecase/` — one class, one operation, business rules here (see
   `RegisterFineUseCase` for the "only ADMIN may write" pattern).
6. Build the ViewModel + UiState + Screen in `ui/<name>/`.
7. Register the screen's route in `core/navigation/component/Destinations.kt` and wire it
   into `MainNavHost.kt` (see `project-navigation` skill) — new features are almost always
   added to `MainNavHost`, not `AuthNavHost`.
8. If the feature needs a bottom-nav entry, add it to `BOTTOM_BAR_ITEMS` in
   `Destinations.kt` and to `MAIN_ROUTES`.

## Where to put a new screen inside an existing feature

Same idea at smaller scale: new `ui/<screenname>/` folder with `Screen.kt` + `UiState.kt` +
`ViewModel.kt` (+ `components/` if it has sub-composables), route added to
`Destinations.kt`, entry added to the relevant `NavHost`.

## Layering rule

`data` and `domain` must not import from `ui`. **This rule is currently violated** in
`feature/fine/domain/model/Fine.kt`, which imports `FineFormState` from
`feature/fine/ui/fine_form` for its `toForm()`/`toDomain()` mapper extensions. Don't repeat
this in new code — if you need a UI-facing mapper, put the mapper function in the `ui` layer
file instead of the `domain` layer file, or accept the existing exception without spreading
it further.
