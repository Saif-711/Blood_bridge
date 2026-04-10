# BloodBridge — Architecture & feature playbook

Senior-style guide for adding features **without overengineering**. Stack: **Spring Boot (JWT, Security, JPA)** + **React**.

Use this document in two ways:

1. **Methodology** — repeat these steps for any new capability (`[PUT FEATURE HERE]`).
2. **Worked example** — *“Ownership: tie BloodRequest & Donor to User”* (recommended next step for BloodBridge).

**Arabic product roadmap (items 2–7):** geo matching, radius expansion, eligibility, QR check-in, role dashboards, i18n — see **[PLANNED_FEATURES_AR.md](./PLANNED_FEATURES_AR.md)**.

---

## 1. Clean architecture (practical, not ceremonial)

BloodBridge uses a **layered** style that scales well for a typical REST API:

| Layer | Responsibility | Rule of thumb |
|--------|----------------|---------------|
| **Controller** | HTTP: paths, methods, status codes, validation entry | Thin: no business rules |
| **Service** | Business rules, transactions, orchestration | All “if / when / who can” lives here |
| **Repository** | Database access (JPA) | Only queries; no HTTP |
| **Entity** | DB row shape, relationships | No JSON/API-specific fields |
| **DTO** | What crosses the API boundary | Request bodies & responses you want stable |

**Why not more folders?**  
Hexagonal / “use case” packages are great at very large scale. For BloodBridge, **package-by-feature** (`Donor/`, `Request/`, `Auth/`) + layers inside each feature is enough. Split only when a package hurts to navigate.

**Flow (simple):**

```
Client → Controller → Service → Repository → DB
              ↓
            DTO in/out (map Entity ↔ DTO in service or small mapper)
```

---

## 2. Step-by-step: design any feature `[PUT FEATURE HERE]`

### Step 1 — Describe the feature in one sentence

*Example:* “Hospitals can see only blood requests they created.”

If you cannot state it in one sentence, split into two features.

### Step 2 — Data model

Ask:

- New **table** / **entity**, or only fields on existing rows?
- **Who owns** the row (user, hospital, system)?
- **Cardinality** (one user → many requests?).

Sketch relationships before code.

### Step 3 — API contract (REST)

- Resources (`/requests`, `/donors`, …)
- Methods (GET list, GET one, POST, PUT/PATCH, DELETE)
- **Auth**: public vs `authenticated()` vs role-based

Write the contract in this doc or OpenAPI later—not only in code.

### Step 4 — Backend shape

For each feature area:

| Piece | Purpose |
|--------|---------|
| `Xxx.java` (Entity) | Persistence + `@ManyToOne` / `@OneToMany` as needed |
| `XxxRepository` | `JpaRepository` + derived/custom queries |
| `XxxService` | Rules, `findById`, ownership checks, `@Transactional` on writes |
| `XxxController` | Maps HTTP → service, returns DTOs where useful |
| `XxxRequestDTO` / `XxxResponseDTO` | Stable API; hide passwords, internal IDs if needed |

### Step 5 — Security

- **JWT** already identifies the user (email in token → `UserDetails`).
- In **service** methods that mutate data, load **current user** from `SecurityContextHolder` and compare to **entity owner** (or role).

Keep SecurityConfig **coarse** (which URLs need login); keep **fine** rules in **service**.

### Step 6 — Frontend (React)

- One **API module** function per endpoint (you already use `api.js`).
- Pages call API; **redirect to login** if 401 (global handler is optional later).

### Step 7 — Ship in slices

1. DB + entity + repo  
2. Service + controller + DTO  
3. Minimal React screen  
4. Then polish (validation, errors, loading)

---

## 3. Database changes (when needed)

### JPA + MySQL

- Prefer **`ddl-auto=update`** in dev only; use **Flyway/Liquibase** when you care about repeatable migrations (add when team grows).
- Adding a column: update **Entity**, restart app (or run migration).

### Naming

- Table names: explicit `@Table(name = "...")` (you already do this).
- Foreign keys: `@JoinColumn(name = "user_id")` on the owning side.

---

## 4. DTOs — keep them boring

| Type | Use for |
|------|---------|
| `*AddDTO` / `*CreateRequest` | POST body |
| `*UpdateDTO` | PUT/PATCH (partial updates optional) |
| `*ResponseDTO` | GET responses when entity has lazy relations or secrets |

**Rules:**

- Do not expose `User.password` in any response DTO.
- Map **Entity → ResponseDTO** in the service (or a tiny mapper class if mapping grows).

---

## 5. Techniques you already have (how to use them)

### JWT + Security

- **Login** returns token; frontend stores it and sends `Authorization: Bearer …`.
- **SecurityConfig**: `permitAll` for `/auth/**`; `authenticated()` for business APIs.
- **JwtAuthFilter**: validates token and fills `SecurityContextHolder`.

**Adding ownership:** in service:

```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String email = auth.getName(); // or cast to UserDetails
// load User by email, compare user.getId() to entity.getOwner().getId()
```

### Transactions

- Put `@Transactional` on **service** methods that **write** multiple rows or read-then-write.

### Validation

- `@Valid` on controller parameters + `jakarta.validation` annotations on DTOs when you want declarative checks.

---

## 6. Worked example (concrete): **Link BloodRequest & Donor to User**

*This replaces a vague `[PUT FEATURE HERE]` with the next logical product step.*

### Goal (one sentence)

Every blood request and donor profile belongs to the **logged-in user**; users only manage **their** rows (unless you later add admin).

### 6.1 Database / entity changes

**`blood_requests`**

- Add nullable first for safer rollout: `user_id BIGINT` FK → `users.id`
- After backfill (if any), make `nullable = false` if every row must have an owner

**`donors`**

- Same: `user_id BIGINT` FK → `users.id`

**JPA (conceptual)**

```java
// On BloodRequest and Donor:
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private User owner; // import from Auth package; mind package cycles (see below)
```

**Package cycles:** if `User` must not depend on `Donor`, keep `User` free of `@OneToMany` at first, or move shared types to a small `common` package. Simplest first: only `ManyToOne` from Donor/BloodRequest → `User`.

### 6.2 Repository

- `List<BloodRequest> findByOwner_Id(Long userId);`
- `Optional<Donor> findByOwner_Id(Long userId);` (if one donor per user)

### 6.3 Service logic (simple)

- **Create request/donor:** resolve current user from security context → set `owner`.
- **List “mine”:** `findByOwner_Id(currentUserId)`.
- **Update/delete:** load by id → if `owner` ≠ current user → throw `AccessDeniedException` or return 404 (your choice; 404 leaks less).

### 6.4 Controller

- `POST /requests` — unchanged path; service attaches owner.
- `GET /requests/mine` — new endpoint (clearer than overloading `GET /requests` for two meanings).
- Same pattern for donors.

### 6.5 DTOs

- **Request DTOs** stay as today (`bloodType`, `hospital`, …); **do not** accept `userId` from client (prevents spoofing). Owner always from token.

### 6.6 SecurityConfig

- No change required if URLs stay under authenticated routes.

### 6.7 React

- After login, call `GET /requests/mine` for dashboard list.
- Forms keep posting the same JSON; backend ignores client-sent `userId`.

### 6.8 Why this stays simple

- One FK per aggregate root.
- No event bus, no CQRS—just **foreign key + service check**.

---

## 7. Blank template for your next feature

Copy and fill:

```markdown
## Feature name: ______________________

**One-sentence goal:**

**Tables / columns:**

**API:**
- METHOD path — who can call

**Entity changes:**

**Repository methods:**

**Service rules (bullet list):**

**DTOs:**

**Frontend screens / api.js functions:**

**Out of scope (for later):**
```

---

## 8. Anti-patterns (avoid)

- Putting **business rules** in controllers or repositories.
- Letting the client send **`userId`** for ownership (use JWT).
- Returning **entities** directly when they gain relations (N+1, leaked fields).
- **Giant “God” service** — split by feature when > ~300 lines or mixed concerns.

---

## 9. When to “improve” beyond this doc

| Trigger | Improvement |
|---------|-------------|
| Many migrations | Flyway |
| Repeated mapping | MapStruct or dedicated mappers |
| Complex rules | Domain methods on entity (`request.canBeCancelledBy(user)`) |
| Many integrations | Introduce interface + adapter behind service |

---

## Related files in this repo

- Backend entry: `Backend/src/main/java/blood/bridging/donating/`
- Security: `Config/SecurityConfig.java`, `Config/JwtAuthFilter.java`
- Frontend API: `frontend/src/services/api.js`

---

*Document version: aligned with BloodBridge packages `Auth`, `Donor`, `Request`.*
