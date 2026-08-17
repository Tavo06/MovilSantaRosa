---
name: project-models-repositories
description: MovilSantaRosa domain/data model split, mapper conventions, and repository interface/impl pattern. Use when adding or changing a model, repository, or use case.
---

# MovilSantaRosa — Models, Mappers, Repositories, Use Cases

## The two-model split

Every entity has exactly two representations, never one shared model:

- **`data/model/XModel.kt`** — the Firebase DTO. `@Serializable data class`, every field
  defaulted (`val amount: Double = 0.0`, `val status: FineStatus = FineStatus.PENDING`,
  etc.). This shape must exactly match what's stored under the entity's Realtime Database
  node, because it's deserialized via `DataSnapshot.getValue(XModel::class.java)`, which uses
  reflection + a no-arg constructor (only possible because every property has a default).
  Nested enums are also `@Serializable` and live in the **same file** as the model they
  belong to (e.g. `FineReason`, `FineStatus` in `FineModel.kt`), each with a `displayName:
  String` computed property holding the Spanish UI label — this is the single source of
  truth for how an enum value is displayed, don't duplicate the `when` elsewhere.
- **`domain/model/X.kt`** — the app-facing type used by use cases, ViewModels, and screens.
  Same field set as `XModel` conceptually, but not `@Serializable` (except where it needs to
  cross a `NavKey`/result boundary) and with `currentTimeMillis()` (not `0L`) as the default
  for timestamp fields, so a freshly-constructed domain object has a sensible "now" instead
  of epoch zero.

## Mappers

`fun XModel.toDomain(): X` and `fun X.toModel(): XModel` live together in
`domain/model/X.kt`, as simple field-by-field constructor calls (no mapping library). If the
UI form has its own state shape (`XFormState`, see `project-compose-kotlin` skill), its
`toDomain()`/`toForm()` mappers are conventionally placed in the **same domain model file**
too — see `Fine.kt`, which has `FineModel.toDomain()`, `Fine.toModel()`, `Fine.toForm()`,
and `FineFormState.toDomain()` all together. This is a known layering wrinkle (domain
importing a UI type) — see `project-architecture` skill's "Layering rule" section. Don't
expand it into new features; if you need it, ask before repeating the pattern, or place the
`XFormState` mapper in the `ui` layer file instead.

Where a form field is a `String` in the UI (e.g. `amount: String` for a numeric text field),
the domain conversion uses `toDoubleSafe()` from `core/utils/StringUtil.kt` (returns `0.0` on
parse failure — validation happens earlier in the ViewModel, not in the mapper).

## Repository interface + impl

- Interface in `domain/repository/XRepository.kt`: plain suspend functions returning
  `Result<T>` for one-shot operations, `Flow<List<T>>` (no `Result` wrapper — errors close
  the flow) for live list reads. See `feature/fine/domain/repository/FineRepository.kt` for
  the canonical shape:
  ```kotlin
  interface FineRepository {
      fun getFinesByEmail(email: String): Flow<List<Fine>>
      fun getAllFines(): Flow<List<Fine>>
      suspend fun getFineById(id: String): Result<Fine>
      suspend fun registerFine(fine: Fine): Result<Unit>
      suspend fun updateFine(fine: Fine): Result<Unit>
  }
  ```
- Implementation in `data/repository/XRepositoryImpl.kt`, `@Inject constructor(private val
  database: FirebaseDatabase)` (or `FirebaseAuth`, or `@ApplicationContext Context` for
  device-API repositories like `LocationRepositoryImpl`). See `project-firebase` skill for
  the exact Firebase call patterns to copy.
- Bound in `di/XModule.kt`:
  ```kotlin
  @Module @InstallIn(SingletonComponent::class)
  abstract class RepositoryModule {
      @Binds @Singleton
      abstract fun bindXRepository(impl: XRepositoryImpl): XRepository
  }
  ```
  Note: most feature `di` modules name this class `RepositoryModule` (a per-file/per-package
  name, not a global one — no collision because they're in different packages). Some
  `@Binds` methods add `@Singleton`, some don't (`FineModule`'s binding is `@Singleton`,
  `LocationModule`'s isn't) — this is inconsistent in the existing code; default to adding
  `@Singleton` for new repository bindings unless there's a reason for a fresh instance per
  injection site.

## Use cases

One class per operation, `domain/usecase/VerbNounUseCase.kt`, `@Inject constructor(private
val repository: XRepository, ...)`, single public entry point:
```kotlin
class RegisterFineUseCase @Inject constructor(
    private val repository: FineRepository,
    private val currentUserUseCase: CurrentUserUseCase
) {
    suspend operator fun invoke(fine: Fine): Result<Unit> {
        val user = currentUserUseCase().getOrElse {
            return Result.failure(Exception("No existe una sesión activa."))
        }
        if (user.role != UserRole.ADMIN) {
            return Result.failure(Exception("No tienes permisos para registrar multas."))
        }
        return repository.registerFine(fine.copy(createdBy = user.email, createdAt = fine.createdAt))
    }
}
```
This is the pattern for role-gated writes: `RegisterFineUseCase`/`UpdateFineUseCase` and
`RegisterPaymentUseCase`/`UpdatePaymentUseCase` both inject `CurrentUserUseCase`, check
`role == ADMIN`, stamp `createdBy`/`createdAt`, then delegate to the repository. Copy this
exactly for any new role-gated write — don't put the role check in the ViewModel.
**Inconsistency to be aware of**: `RegisterPostUseCase`/`UpdatePostUseCase` do **not** have
this check — they call the repository directly with no `CurrentUserUseCase`/role
verification at all, unlike the fine and payment use cases. Don't assume post-registration
is role-gated; if you're asked to make posts admin-only, this check needs to be added, not
just copied from a use case that already has it.

Read-only use cases (`GetFinesUseCase`, `GetAllMembersUseCase`, etc.) are typically a
one-line pass-through: `operator fun invoke() = repository.getAllFines()` (no role check,
since reads are open to any logged-in user in this app). Composite use cases exist too —
`GetHomeStatsUseCase` injects three other use cases and `combine()`s their flows rather than
talking to a repository directly; this is the exception, used only for the dashboard
aggregation, not a pattern to reach for by default.

## Adding a new entity — minimal checklist

1. `data/model/XModel.kt` — `@Serializable data class`, all fields defaulted, enums in the
   same file with `displayName`.
2. `domain/model/X.kt` — domain data class (timestamps default to `currentTimeMillis()`),
   `toDomain()`/`toModel()` mappers in the same file.
3. `domain/repository/XRepository.kt` — interface, `Result<T>`/`Flow<List<T>>` methods.
4. `data/repository/XRepositoryImpl.kt` — Firebase implementation (see `project-firebase`
   skill for exact call shapes).
5. `di/XModule.kt` — `@Binds` the impl to the interface.
6. `domain/usecase/*UseCase.kt` — one per operation; role-gate writes via
   `CurrentUserUseCase` if the entity should be admin-only to mutate.
