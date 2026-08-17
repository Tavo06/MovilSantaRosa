---
name: project-ui-system
description: MovilSantaRosa design system - theme, colors, and the core/uicomponents inventory. Use before building any new UI element; reuse an existing component instead of creating a duplicate.
---

# MovilSantaRosa — UI/UX System

**Rule zero: before writing a new button, dialog, text field, list state, or search bar,
check this inventory and `core/uicomponents/` — reuse or extend an existing component. Do
not create a parallel one-off implementation.**

## Theme

`core/theme/Theme.kt`: `MovilSantaRosaTheme(darkTheme = isSystemInDarkTheme(), dynamicColor =
false, content)`. Material 3 only — there is no Material 2 dependency in this project.
`dynamicColor` defaults to `false` (Material You dynamic color is available in code but
disabled by default; don't flip this default without asking). Two hand-authored
`ColorScheme`s (`LightColorScheme`/`DarkColorScheme`), no `Color.kt` constants file separate
from `Theme.kt` — colors are defined inline in the scheme builders. `core/theme/Type.kt`
holds the `Typography`. Always reference colors via `MaterialTheme.colorScheme.*` /
`MaterialTheme.typography.*` in new composables, never hardcode a `Color(0x...)` inline.

Wrap the whole app once, at `MainActivity`'s `setContent { MovilSantaRosaTheme { AppNavHost()
} }` — don't re-wrap individual screens in `MovilSantaRosaTheme`. `@Preview` composables use
plain `MaterialTheme { }` instead (see every `XScreenPreview` for the pattern) — this is
deliberate so previews render even if `MovilSantaRosaTheme` needs a real `Context`.

## `core/uicomponents/` inventory

| File | Component | Use for |
|---|---|---|
| `AppTopBar.kt` | `AppTopBar` | Per-route top bar, `when`-switches on the current top-level destination |
| `TopBarHeader.kt` | `TopBarHeader` | The generic titled top bar used by most `AppTopBar` branches |
| `AppHeader.kt` / `AppHeaderTransparent.kt` | `AppHeader`, `AppHeaderTransparent` | Screen headers; transparent variant used over full-bleed content like the map picker |
| `AppBottomBar.kt` | `AppBottomBar` | Bottom nav bar built from `BOTTOM_BAR_ITEMS` |
| `AppFloatingActionButton.kt` | `AppFloatingActionButton` | The single "create" FAB, visibility/action depends on route + `UserRole` |
| `AppPrimaryButton.kt` | `AppPrimaryButton` | Standard filled action button (form submit, etc.) |
| `AppSearchBar.kt` | `AppSearchBar` | List-screen search field — sanitizes input (strips newlines, caps at 20 chars) |
| `FormTextField.kt` | `FormTextField` | All text inputs: label/placeholder/error/counter/password-visibility-toggle support built in — use this instead of a raw `OutlinedTextField` |
| `FormDropdown.kt` | `FormDropdown<T>` | Generic enum/option picker (`ExposedDropdownMenuBox`) — pass `options`, `labelProvider` |
| `FormDatePicker.kt` | (date picker) | Date fields, e.g. fine due date |
| `FormSection.kt` | `FormSection` | Groups related form fields with a section label |
| `CardContent.kt` | `CardContent` | Generic card wrapper used by list item components |
| `EmptySection.kt` | `EmptySection` | "No items" empty state for lists |
| `ErrorSection.kt` | `ErrorSection` | Error state for lists/screens, with an optional `onRetry` |
| `BoxShimmer.kt` | `BoxShimmer` | Shimmer placeholder used to build `SkeletonSection` |
| (skeleton section, used as `SkeletonSection` in `FinesScreen`) | `SkeletonSection` | Loading state for lists — built from `BoxShimmer` |
| `MessageDialog.kt` | `MessageDialog` | The only alert dialog in the app — used for both success and error messaging (see `<Name>FormActionHandler` composables) |
| `ProgressIndicator.kt` / `ProgressIndicatorOverlay.kt` | spinner / full-screen overlay spinner | In-progress states (e.g. resolving an address in the location picker) |
| `SplashScreen.kt` | `SplashScreen` | Shown while `MainViewModel.session` is `SessionState.Loading` |

## Per-screen UI pattern (list screens)

```kotlin
Column(modifier.padding(...).fillMaxSize()) {
    when (val state = uiState) {
        XUiState.Loading -> SkeletonSection(Modifier.fillMaxSize())
        is XUiState.Error -> ErrorSection(title = "Ups, tenemos inconvenientes", description = state.message)
        is XUiState.Success -> {
            AppSearchBar(query = state.query, onQueryChange = viewModel::updateQuery, placeholder = "Buscar ...")
            XList(items = state.items, onClick = onItemClick, modifier = Modifier.fillMaxWidth().weight(1f))
        }
    }
}
```
`ErrorSection`'s title is the fixed Spanish string `"Ups, tenemos inconvenientes"` across
screens — reuse that exact copy for consistency rather than writing a new title per screen.

## Per-screen UI pattern (form screens)

Form body lives in a `<Name>FormContent.kt` composable (fields via `FormTextField`/
`FormDropdown`/`FormDatePicker`/`FormSection`), submit button is `AppPrimaryButton`, and a
`<Name>FormActionHandler(action = uiState.actionState, onSuccess = ..., onReset =
viewModel::resetAction)` composable is placed alongside the content to react to
`ActionState.Success` (triggers navigation back) and `ActionState.Error` (shows a
`MessageDialog`). Copy `FineFormActionHandler`/`FineFormContent` structure for new forms.

## Language and copy

All user-facing strings observed in the codebase are **Spanish**, written inline in
Kotlin (there is no `strings.xml`-based i18n for feature copy — `strings.xml` only holds
`app_name` and Android-required framework strings). Match this: write new UI copy in
Spanish, inline, in the same tone as existing screens (short, direct, e.g. "Seleccione un
socio.", "No se pudo cargar la multa").

## Map UI

The only map screen (`feature/post/ui/location_picker/LocationPickerScreen.kt`) uses
`AppHeaderTransparent` over the map instead of `AppTopBar`, and its own `MapControls`
composable (`feature/post/ui/location_picker/components/MapControls.kt`) — a vertical FAB
stack (select/zoom in/zoom out) — rather than reusing `core/uicomponents` buttons, since
Material FABs floating over a map don't fit the list-screen chrome. If you add more map
screens, reuse `MapControls` rather than building another FAB stack from scratch.
