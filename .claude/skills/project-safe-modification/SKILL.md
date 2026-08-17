---
name: project-safe-modification
description: Ground rules for changing code in MovilSantaRosa - analyze before modifying, reuse before creating, minimize diffs. Use before starting any code change in this repo, and re-check before finishing.
---

# MovilSantaRosa — Safe Modification Rules

This is a working, feature-complete app with an established, consistent pattern across 6
features. The value of that consistency is high — a change that works but doesn't match the
existing pattern makes every future change harder. Load `project-architecture` and whichever
topic skill matches the change (compose, firebase, navigation, models/ui) before writing
code.

## Before writing any code

1. Find the most similar existing implementation and read it in full. For a new
   list/form/picker feature, that's almost always `feature/fine/*` or `feature/payments/*`
   (the two most complete, symmetrical features). For a UI element, check
   `core/uicomponents/` first (see `project-ui-system` skill's inventory table) — assume a
   component you need already exists until you've actually checked.
2. Identify which layer the change belongs in: Firebase call → `data/repository`; business
   rule / authorization → `domain/usecase`; screen state → `ui/*ViewModel` +
   `*UiState`; visual only → `ui/*Screen` + `core/uicomponents`. Don't put a Firebase call in
   a ViewModel, or a role check in a Composable.
3. Check `CLAUDE.md`'s Known Issues section — if the task touches one of those areas, decide
   explicitly whether you're fixing the known issue (only if asked) or just working around
   it without spreading the anti-pattern further.

## While making the change

- Match existing naming: `X`/`XModel` for models, `XRepository`/`XRepositoryImpl`,
  `XUseCase` (verb-noun), `XViewModel`, `XUiState`, `XScreen`. Don't invent a different
  suffix convention for a new feature.
- Match existing Spanish user-facing copy style — short, direct sentences (see
  `project-ui-system` skill).
- Reuse `core/uicomponents/*` — do not create a second `FormTextField`-like component or a
  second alert dialog; extend the existing one with a new parameter if it's missing a
  capability, rather than duplicating it.
- Keep business rules in use cases, not ViewModels or Composables — this is the one
  architectural line this codebase draws consistently; don't blur it.
- Don't add Room or Ktor usage without raising it with the user first — both are dependencies
  today with zero call sites; using either is a real architectural decision (introducing
  local persistence or a network client), not a small addition.
- Don't add a new state-management library, a new nav library, a new DI framework, or switch
  Material versions. Everything needed already exists in `libs.versions.toml`.
- Minimize the diff: don't reformat, reorder imports, or "clean up" files you're not
  otherwise changing. Don't refactor a working pattern (e.g. the `filteredX` dead-getter
  duplication) as a drive-by while doing unrelated work — call it out to the user instead.

## Explicitly do NOT, without being asked

- Rewrite the architecture (e.g. moving to a single shared `Repository` base class, adding a
  generic `Resource<T>` wrapper, introducing a different state pattern) — the current
  per-feature symmetry is a deliberate, working structure even though it repeats some
  boilerplate across features.
- Replace MapLibre with Google Maps Compose, or Navigation 3 with Navigation 2 Compose, or
  any other "I'd prefer X library" swap.
- Bump `compileSdk`/`targetSdk`/`minSdk`, AGP, Kotlin, or Compose BOM versions.
- Rename existing classes/files for style reasons.
- Delete `HomeRepository.kt` or other dead code spotted during analysis unless the user asks
  — flag it, don't remove it unprompted.
- Add Firebase Firestore/Storage/Analytics/Crashlytics/Remote Config — only Auth + Realtime
  Database are used today.
- Touch `.gitignore`'s commented-out `google-services.json` lines, or the committed
  `app/google-services.json` file, without asking — its git status appears deliberate.

## After making the change

1. Verify imports resolve to real, existing symbols — this codebase has several
   near-duplicate names across features (`RepositoryModule` in multiple `di/` packages,
   `FineModel`/`FineReason`/`FineStatus` vs domain `Fine`) — double check you're importing
   from the intended package, not a same-named symbol in a sibling feature.
2. Run `./gradlew assembleDebug` (or `gradlew.bat assembleDebug` on Windows) for anything
   beyond a trivial one-line change; run `./gradlew test` if you touched a `ViewModel`,
   `UseCase`, or util function with existing test coverage.
3. For UI changes, actually run the app (or at minimum check the `@Preview` composable
   compiles/renders) rather than assuming a Compose layout is correct from reading it — see
   the `run` skill if you need to launch the app.
4. Summarize what changed and why in your response — don't just say "done." If you touched
   something adjacent to a Known Issue, say so explicitly rather than letting it look
   incidental.
