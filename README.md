# BloodBridge

Blood donation platform: **Spring Boot** backend (`Backend/`) and **React (Vite)** frontend (`frontend/`). Authentication uses JWT; protected APIs require a logged-in user.

**Project status (what’s done, gaps, docs):** **[PROJECT_STATUS.md](PROJECT_STATUS.md)**

---

## Architecture & how to add features

Step-by-step **layered design**, DTO rules, security patterns, database notes, and a **worked example** (*link requests/donors to the logged-in user*) live here:

**[docs/ARCHITECTURE_AND_FEATURES.md](docs/ARCHITECTURE_AND_FEATURES.md)**

**ميزات مخططة (عربي + تقني):** التوسيع التلقائي للنطاق، المطابقة الجغرافية، أهلية المتبرع، QR للحضور، لوحات حسب الدور، ثنائية اللغة — راجع  
**[docs/PLANNED_FEATURES_AR.md](docs/PLANNED_FEATURES_AR.md)**

Use the architecture doc as the playbook for `[PUT FEATURE HERE]`; keep the root README for quick links and the checklist below.

---

## Next steps

### 1. Auth & onboarding

- [ ] Implement **Register** page: form wired to `POST /auth/register`, then redirect to login (or auto-login if you add it).
- [ ] After login, optional **redirect to the page the user wanted** (e.g. return URL query or state).
- [ ] Show **logout** (or user menu) in a shared layout, not only on the dashboard.
- [ ] Handle **401 / expired token**: clear token, redirect to login with a short message.

### 2. Data ownership (backend + database)

- [ ] Link **blood requests** and **donors** to the authenticated **user** (e.g. `userId` foreign key).
- [ ] Add endpoints such as **“my requests”** / **“my donor profile”** so users only manage their own rows (unless you add admin roles).

### 3. Roles (only if the product needs them)

- [ ] Define roles (e.g. hospital vs donor vs admin) in the database or JWT claims.
- [ ] Restrict who can create requests vs register as donor vs see global lists.

### 4. Frontend polish

- [ ] Shared **navigation** (Dashboard, Request blood, Add donor, Log out) on inner pages.
- [ ] **Loading** spinners and clearer **error** messages on forms.
- [ ] Client-side **validation** (blood type format, required fields).

### 5. Configuration & deployment

- [ ] Move API base URL to **environment variables** (no hardcoded `localhost` in production builds).
- [ ] Tighten **CORS** to your real frontend origin when deployed.
- [ ] Store **JWT secret** and DB credentials in env vars, not only in `application.properties` in repo.

### 6. Quality

- [ ] Consistent **JSON error responses** from the backend.
- [ ] Add a few **integration tests** (login + one protected `POST`).
- [ ] Remove **console logs** of tokens before production.

---

## Local development (quick reference)

| Piece        | Typical command / URL        |
|-------------|------------------------------|
| Backend     | Run Spring Boot (port **8081** per `application.properties`) |
| Frontend    | `npm run dev` in `frontend/` (often **http://localhost:5173**) |

Ensure MySQL is running and matches `Backend/src/main/resources/application.properties`.

---

## Suggested order

1. Finish **Register** + auth UX (items in section 1).  
2. **Link entities to users** (section 2).  
3. **Shared layout** + loading/errors (section 4).  
4. **Roles** (section 3) if required.  
5. **Deploy & config** (section 5) + **quality** (section 6).

Adjust this list if your goal is a small demo vs a production-ready app.
