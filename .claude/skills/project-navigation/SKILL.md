---
name: project-navigation
description: MovilSantaRosa's custom Navigation 3 setup - routes, back stacks, bottom sheets, and how to add a new screen. Use when adding a screen, changing navigation, or passing data/results between screens.
---

# MovilSantaRosa — Navigation

Built on **Navigation 3** (`androidx.navigation3`, alpha), with a hand-rolled layer on top
(`core/navigation/component/{Navigator,NavigationState}.kt`) that Navigation 3's own sample
apps also use — this is not fully custom-invented, but it is not the "just use
`NavController`" pattern from Navigation 2.x either. Don't introduce
`androidx.navigation.compose` (Navigation 2) — it isn't a dependency here.

## Routes: `Destinations.kt`

`core/navigation/component/Destinations.kt` defines every route as a `@Serializable`
`NavKey` — `data object` for no-arg routes (`HomeDestination`, `FinesDestination`), `data
class` for parameterized ones (`FineFormDestination(val fineId: String? = null)`,
`FinePickerDestination(val memberEmail: String)`). A `null`/absent id conventionally means
"create mode" for form destinations — see `project-compose-kotlin` skill's form-screen
pattern.

Three route sets, also in this file:
- `AUTH_ROUTES = {LoginDestination, RegisterDestination}`
- `MAIN_ROUTES = {HomeDestination, MembersDestination, FinesDestination,
  PaymentsDestination, PostDestination}` — these are the bottom-nav top-level routes
- `BOTTOM_BAR_ITEMS: Map<NavKey, NavBarItem>` — icon + label for each `MAIN_ROUTES` entry,
  rendered by `AppBottomBar`

## Nav graphs

- `AppNavHost.kt` — the root. Collects `MainViewModel.session` and switches between
  `SplashScreen()` (Loading), `AuthNavHost` (LoggedOut), `MainNavHost(userRole, onLogout)`
  (LoggedIn). This is the only place session state gates navigation.
- `AuthNavHost.kt` — `LoginDestination` ↔ `RegisterDestination`, no bottom bar, plain
  `NavDisplay`.
- `MainNavHost.kt` — everything post-login: builds the `entryProvider` (one `entry<Route>` per
  destination), wraps it in a `Scaffold` with `AppTopBar`/`AppBottomBar`/
  `AppFloatingActionButton`, renders via `NavDisplay` with `BottomSheetSceneStrategy`
  registered. **This is where you add a new screen's `entry<...>` block.**

## `Navigator` / `NavigationState`

`rememberNavigationState(startRoute, topLevelRoutes)` builds one `NavBackStack` per
top-level route (so switching bottom-nav tabs preserves each tab's stack) plus a
`topLevelRoute` `MutableState`. `Navigator` (plain class, not a ViewModel — created once per
`NavHost` with `remember { Navigator(navigationState) }`) exposes:
- `navigate(route)` — if `route` is itself a top-level route, switches tabs; otherwise
  pushes onto the current tab's back stack.
- `goBack()` — pops the current tab's stack, or returns to `startRoute` if already at the
  tab root.
- `reset(route)` — clears all stacks (used for e.g. post-logout reset; check current call
  sites before reusing).

Don't call `NavBackStack` methods directly from screen code — always go through the
`Navigator` instance passed down (or captured via closure) from the `NavHost`.

## Adding a new screen — checklist

1. Add a route to `Destinations.kt` (`data object` or `data class`, `@Serializable`, extends
   `NavKey`). If it's a new bottom-nav tab, also add it to `MAIN_ROUTES` and
   `BOTTOM_BAR_ITEMS`.
2. Add `entry<YourDestination> { ... }` inside `MainNavHost.kt`'s `entryProvider` block
   (or `AuthNavHost.kt` if it's pre-login).
3. For a list/detail screen with no extra setup, the entry body is just the `@Composable`
   call: `entry<FinesDestination> { FinesScreen(onFineClick = { navigator.navigate(...) }) }`.
4. For a form screen that needs setup before first composition (mode selection, loading an
   existing entity, receiving a picker result), build the `hiltViewModel()` **inside the
   `entry` block** and drive it with `LaunchedEffect`, exactly like the existing forms:
   ```kotlin
   entry<FineFormDestination> { destination ->
       val viewModel: FineFormViewModel = hiltViewModel()
       ResultEffect<Member> { member -> viewModel.selectMember(member) }
       LaunchedEffect(destination.fineId) {
           if (destination.fineId == null) viewModel.setMode(FineFormMode.CREATE)
           else viewModel.loadFine(destination.fineId)
       }
       FineFormScreen(viewModel = viewModel, onBackClick = { navigator.goBack() }, ...)
   }
   ```
5. For a modal bottom sheet (picker screens), add `metadata =
   BottomSheetSceneStrategy.bottomSheet()` to the `entry<...>(...)` call — see
   `entry<MemberPickerDestination>` / `entry<FinePickerDestination>` in `MainNavHost.kt`.

## Passing data between screens

Two different mechanisms are used, for two different situations — don't mix them up:

- **Nav args** (simple values known at navigate-time): put them as constructor params on the
  `NavKey` data class, e.g. `FinePickerDestination(memberEmail: String)`, read via
  `destination.memberEmail` in the `entry` block.
- **Picker results** (a screen picks something and hands it back to the screen that opened
  it): use `LocalResultEventBus` + `ResultEffect<T>`, not a shared ViewModel or a nav arg.
  The picker screen does `resultBus.sendResult(result = member)` then `navigator.goBack()`;
  the origin screen's `entry` block has `ResultEffect<Member> { member ->
  viewModel.selectMember(member) }`. This is how `MemberPickerDestination`,
  `FinePickerDestination`, and `PickerMapDestination` all hand data back. See
  `core/navigation/results/NavigationResults.kt` for the underlying plumbing if you need to
  add a new result type.

## Top bar / bottom bar / FAB per route

`AppTopBar` and `AppFloatingActionButton` both `when`-switch on the current top-level
`NavKey` to decide title/visibility/action (e.g. FAB navigates to
`FineFormDestination()`/`PaymentFormDestination()`/`PostFormDestination()` depending on
`navigationState.topLevelRoute`, and is hidden entirely for routes with no "create" action).
When adding a new top-level route that needs a title or a FAB action, extend these `when`
blocks — don't build a per-screen top bar inside the screen itself.
