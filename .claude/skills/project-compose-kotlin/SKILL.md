---
name: project-compose-kotlin
description: MovilSantaRosa Kotlin/Jetpack Compose conventions - state management, ViewModel/UiState patterns, recomposition rules actually used in this codebase. Use when writing or editing any @Composable, ViewModel, or UiState.
---

# MovilSantaRosa — Kotlin + Compose Conventions

These are the patterns this codebase actually uses, observed directly in the code (mainly
`feature/fine/*` and `feature/payments/*`, which are the most complete). Follow them for
consistency rather than introducing a different Compose style.

## ViewModel shape

- `@HiltViewModel class XViewModel @Inject constructor(...)`, constructor-injects use
  cases directly (not repositories) — see `FineViewModel(getFinesUseCase: GetFinesUseCase)`.
- Exactly one exposed `StateFlow` per screen, named `uiState` (list screens) — no separate
  event/effect channel is used anywhere in this codebase; one-off effects (navigate on
  success, show error dialog) are handled by a dedicated `<Name>ActionHandler` composable
  reacting to an `ActionState` field inside the same `UiState` (see "Form screens" below).
- List screens: private `MutableStateFlow` for local UI-only state (e.g. `query`),
  `combine()`d with the use case's `Flow`, `.stateIn(viewModelScope,
  SharingStarted.WhileSubscribed(5000), initialValue = XUiState.Loading)`. Example:
  `FineViewModel.uiState`.
- Form screens: single `MutableStateFlow<XFormUiState>` mutated with `.update { }`, no
  `combine`/`stateIn` — the whole form state (including nested `form: XFormState`,
  `actionState: XFormActionState`, `mode: XFormMode`) lives in one state object. Example:
  `FineFormViewModel`.
- Mutating functions on the ViewModel take either primitive params (`updateQuery(query:
  String)`) or a transform lambda matching the pattern
  `fun updateForm(transform: XFormState.() -> XFormState)`, used from the nav layer like:
  ```kotlin
  viewModel.updateForm { copy(latitude = ..., longitude = ..., address = ...) }
  ```
- Never expose `MutableStateFlow` publicly — always back with `_uiState` /
  `.asStateFlow()`.

## UiState shape

- `sealed class XUiState` (or `sealed interface`) with `Loading`, `Success(...)`, `Error(message:
  String)` variants for list screens. Use `data object` for the parameterless cases (project
  targets Kotlin 2.2, `data object` is standard here — don't use plain `object` for these;
  one legacy screen, `PostUiState`, still uses plain `object Loading`, but write new code with
  `data object`).
- **Do not** add a computed `filteredX: List<X>` getter on the `Success` state that
  duplicates filtering already done in the ViewModel's `combine` block — this exists in both
  `FineUiState.Success.filteredFines` and `PaymentUiState.Success.filteredPayments` as dead
  code (nothing calls them; the screen reads `state.fines` / `state.payments` directly,
  already filtered). Filter once, in the ViewModel.
- Form screens use a flat top-level state (`XFormUiState`) containing a nested pure-data
  `XFormState` (the actual field values, all `String`/simple types so text fields bind
  directly — see `FineFormState.amount: String`, converted via `toDoubleSafe()` only at
  submit time), an `XFormActionState` sealed interface (`Idle`/`Loading`/`Success`/
  `Error(message)`), and an `XFormMode` enum (`CREATE`/`EDIT`/`READ_ONLY`) with
  `displayName`/`displayButton` computed properties for screen title/button text.

## Composable screen shape

- Top-level `@Composable fun XScreen(modifier: Modifier = Modifier, ..., viewModel: XViewModel
  = hiltViewModel())` — ViewModel obtained via `hiltViewModel()` default parameter, not
  passed from the nav graph, **except** form screens, where the ViewModel is created in the
  `NavHost`'s `entry<...>` block (so `LaunchedEffect`/`ResultEffect` in the nav layer can
  call methods on it before the screen composes) and passed in as a parameter — compare
  `FinesScreen` (`viewModel = hiltViewModel()` default) vs `FineFormScreen(viewModel: ...)`
  (required param, built in `MainNavHost.kt`).
- Collect state with `collectAsStateWithLifecycle()`, never `collectAsState()`.
- `when (val state = uiState) { ... }` exhaustive branch over the sealed UiState, one
  `@Composable` call per branch (`SkeletonSection`/`ErrorSection`/success content) — see
  `project-ui-system` skill for the shared components used here.
- Every screen file has a `@Preview(showBackground = true, showSystemUi = true)` composable
  below the real screen with hand-built fake data, wrapped in `MaterialTheme { ... }` (not
  `MovilSantaRosaTheme` — previews use plain `MaterialTheme`). Keep adding these for new
  screens; it's the project's only form of visual regression check.

## State inside composables

- `remember`/`mutableStateOf` is used only for genuinely local, non-survivable UI state
  (e.g. `FormTextField`'s password-visibility toggle uses `rememberSaveable`; dropdown
  open/closed state in `FormDropdown` uses plain `remember`). Anything that needs to survive
  navigation or represents domain data goes in a ViewModel's `StateFlow`, not composable
  state.
- `LaunchedEffect` is used for: (a) one-shot side effects keyed on a nav argument, e.g.
  `LaunchedEffect(destination.fineId) { if (fineId == null) viewModel.setMode(CREATE) else
  viewModel.loadFine(fineId) }` in `MainNavHost.kt`; (b) reacting to `ActionState.Success` to
  trigger navigation, inside `<Name>FormActionHandler` composables; (c) permission-request
  flows in `LocationPermission.kt`. Keep the same granularity: one `LaunchedEffect` per
  concern, keyed on the specific value that should retrigger it — not a bare `Unit` key
  unless it truly should only run once.

## Business logic placement

Never put Firebase calls, role checks, or validation logic directly in a `@Composable` or in
a ViewModel body inline — put it in a `UseCase` (validation can live in a private ViewModel
function like `FineFormViewModel.validateForm`, but permission/authorization rules belong in
the UseCase, e.g. `RegisterFineUseCase` checking `user.role != UserRole.ADMIN`).

## Known anti-patterns already in the codebase (don't copy these into new code)

- List-screen repositories' `callbackFlow` listeners call `close(error.toException())` on
  `onCancelled`, but nothing downstream turns that into an `XUiState.Error` — the flow just
  stops emitting. When writing a new list ViewModel, prefer `catch { }` on the use case's
  flow inside `combine`/before `stateIn` if you want errors to actually reach the UI (this
  codebase currently doesn't do this anywhere, so there's no existing pattern to copy for it
  — flag it to the user rather than silently improvising a new error-handling convention).
