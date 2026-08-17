---
name: project-firebase
description: MovilSantaRosa Firebase Realtime Database and Auth structure, repository patterns, and known gaps. Use when reading/writing Firebase data, adding a new Firebase-backed feature, or touching auth/session code.
---

# MovilSantaRosa — Firebase

## What's actually used

- `firebase-auth` — email/password only (`FirebaseAuth.signInWithEmailAndPassword`,
  `createUserWithEmailAndPassword`, `signOut`, `currentUser`).
- `firebase-database` — Realtime Database only. **No Firestore, no Storage** are dependencies
  or used anywhere — don't introduce `FirebaseFirestore`/`FirebaseStorage` calls without
  confirming with the user first, since it would be a new dependency, not an existing one.
- No `google-services.json`-adjacent Firebase Remote Config, Analytics, Crashlytics, FCM,
  etc. — only Auth + Realtime Database are wired into `MovilApp`/DI.

## Root nodes

Each repository declares its own root node as a top-level `const val` next to the class:

| Node | Constant | Declared in | Holds |
|---|---|---|---|
| `user_database` | `USER_DATABASE` | `AuthRepositoryImpl.kt` **and** `MemberRepositoryImpl.kt` (duplicated literal) | User/member records, keyed by Firebase Auth `uid` |
| `fine_database` | `FINE_DATABASE` | `FineRepositoryImpl.kt` | Fine records, keyed by Realtime DB push key |
| `payment_database` | `PAYMENT_DATABASE` | `PaymentRepositoryImpl.kt` | Payment records |
| `post_database` | `POST_DATABASE` | `PostRepositoryImpl.kt` | Announcement/post records |

`user_database` doing double duty as both the auth-profile store and the "members list"
source is intentional in this app — `MemberRepositoryImpl.getAllMembers()` reads the same
node `AuthRepositoryImpl` writes to at registration.

If you add a new Firebase-backed feature, declare its root node the same way: a
package-level `const val SOMETHING_DATABASE = "something_database"` in the
`*RepositoryImpl.kt` file, not in a shared constants file (no such file exists — this
project doesn't centralize node names, so don't invent a central registry unasked).

## DI wiring for Firebase instances

`FirebaseAuth`/`FirebaseDatabase` singletons are provided once, in
`feature/auth/di/LoginModule.kt`:
```kotlin
@Provides @Singleton fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
@Provides @Singleton fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()
```
Every other feature's repository just `@Inject constructor(private val database:
FirebaseDatabase)` — don't re-provide these in a new feature's DI module, inject the
existing singleton.

## Repository patterns — copy these exactly

**Live list read** (`Flow<List<X>>`, used by every list screen):
```kotlin
override fun getAllFines(): Flow<List<Fine>> = callbackFlow {
    val ref = database.reference.child(FINE_DATABASE)
    val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val items = snapshot.children.mapNotNull { it.getValue(FineModel::class.java) }
                .map(FineModel::toDomain)
            trySend(items).isSuccess
        }
        override fun onCancelled(error: DatabaseError) { close(error.toException()) }
    }
    ref.addValueEventListener(listener)
    awaitClose { ref.removeEventListener(listener) }
}
```
Filtered live read uses `.orderByChild("field").equalTo(value)` on the same `ref` before
attaching the listener (see `FineRepositoryImpl.getFinesByEmail`).

**One-shot read** (`suspend fun getById(id): Result<X>`):
```kotlin
val snapshot = database.reference.child(NODE).child(id).get().await()
if (!snapshot.exists()) return Result.failure(Exception("<not found message>"))
val model = snapshot.getValue(XModel::class.java) ?: return Result.failure(Exception("<parse error message>"))
Result.success(model.toDomain())
```
Wrap the whole thing in `try/catch (e: Exception) { Result.failure(Exception(e.message ?:
"<fallback Spanish message>")) }`. **Error messages are user-facing and in Spanish** — match
the existing tone (short, polite, e.g. `"No se pudo registrar la multa."`) for new ones.

**Write (create)**, generating a Realtime DB push key when the domain object has no id yet:
```kotlin
val id = entity.id.ifBlank {
    database.reference.child(NODE).push().key ?: throw Exception("No se pudo generar el identificador.")
}
database.reference.child(NODE).child(id).setValue(entity.copy(id = id).toModel()).await()
```

**Write (update)** — same as create but `id` is already known and used directly.

Every repository method returns `Result<T>` and catches its own exceptions — never let a
Firebase task exception propagate uncaught out of a repository method.

## Models: data model vs domain model

- `data/model/XModel.kt`: `@Serializable data class` mirroring the exact Firebase JSON
  shape, **every field has a default value** (Firebase's `DataSnapshot.getValue(Class)`
  needs a no-arg constructor, which Kotlin only synthesizes when every property is
  defaulted). Nested value classes are plain enums (also `@Serializable`), e.g.
  `FineReason`, `FineStatus`, stored as their enum name string.
- `domain/model/X.kt`: the app-facing type, plus `fun XModel.toDomain(): X` and `fun
  X.toModel(): XModel` in the same file. Timestamps are `Long` epoch millis everywhere
  (`currentTimeMillis()` from `core/utils/DateUtil.kt`), never `Date`/`Instant`/Firebase
  `ServerValue.TIMESTAMP`.
- See `project-models-repositories` skill for the full mapping conventions.

## Auth / session flow

`AuthRepositoryImpl` (`feature/auth/data/repository/AuthRepositoryImpl.kt`):
`login()` signs in via `FirebaseAuth`, then fetches `firstname` from
`user_database/<uid>`; `getCurrentUser()` reads the full profile (`firstname`, `lastname`,
`role`) from the same node, defaulting `role` to `UserRole.PARTNER` if the field is missing
or unparseable. `register()` writes a `RegisterRequest` (password stripped to `""` before
writing) into `user_database/<uid>` after Auth account creation.

Session state at the app root is driven by `MainViewModel.session: StateFlow<SessionState>`
(`Loading` / `LoggedOut` / `LoggedIn(role: UserRole)`), computed by calling
`CurrentUserUseCase()` on `init` and after `refreshSession()`. `AppNavHost` switches between
`AuthNavHost`/`MainNavHost`/`SplashScreen` based on this state — see `project-navigation`
skill.

## Authorization model

Role checks (`UserRole.ADMIN` vs `PARTNER`) are enforced **only in use cases**, client-side
— e.g. `RegisterFineUseCase` and `RegisterPaymentUseCase` both fetch the current user via
`CurrentUserUseCase` and reject with a `Result.failure` if `role != ADMIN` before calling the
repository. **There is no Firebase Realtime Database security-rules file in this repo.**
Treat client-side checks as UX guidance, not real security, and say so if the user asks you
to add a new write path — the actual enforcement needs to exist in the Firebase console
rules, which aren't visible/editable from this codebase.

## Known gaps (don't silently fix; flag if relevant)

- `google-services.json` is committed to git (a commented-out `.gitignore` line shows this
  was deliberate). Don't add/change `.gitignore` rules around it without asking.
- No security rules visible — can't verify server-side authorization from this repo.
- `home/domain/repository/HomeRepository.kt` has no implementation or Hilt binding; it's
  dead code. `GetHomeStatsUseCase` gets its data by composing `GetAllMembersUseCase` /
  `GetFinesUseCase` / `GetAllPostsUseCase` instead of using `HomeRepository`.
