# BloodBridge — Project status

Last updated reflects the repository as of the latest documentation pass. Use this file to see **what exists today** vs **what is planned**.

---

## Summary

| Area | State |
|------|--------|
| **Overall** | Active development — core CRUD and auth work; not production-ready |
| **Backend** | Spring Boot API with JWT security, JPA/MySQL, Donor + Blood Request + Auth modules |
| **Frontend** | React (Vite) with login, dashboard hub, request blood & add donor flows; register UI is a stub |
| **Documentation** | Architecture playbook, Arabic feature roadmap, README with next-step checklist |

---

## What is implemented

### Backend (`Backend/`)

- **Authentication:** `POST /auth/register`, `POST /auth/login`; JWT generation; `User` entity with `role` field (e.g. `USER` / `ADMIN` placeholder).
- **Security:** Stateless JWT filter; most business routes require authentication; CORS configured for `http://localhost:5173`.
- **Donors:** Create donor, list all, list available, filter by blood type (`DonorController` / `DonorService` / `DonorRepo`).
- **Blood requests:** Create and list requests (and by status) via `BloodRequestController` and related service/repository.
- **Persistence:** JPA entities, MySQL via `application.properties` (`ddl-auto=update` in dev).

### Frontend (`frontend/`)

- **Routing:** Home, Login, Register (route exists), Dashboard, Request blood, Add donor.
- **Auth UX:** Login stores JWT in `localStorage`; API helper sends `Authorization: Bearer …` on protected calls.
- **Dashboard:** Hub with links to request blood & add donor; logout; redirect to login if no token.
- **Protected pages:** Request blood and add donor redirect unauthenticated users to login.
- **API client:** Central `services/api.js` for auth, donors, requests.

---

## Partially done / gaps

| Item | Notes |
|------|--------|
| **Register page** | Route exists; UI is a placeholder (`Register.jsx` not wired to `/auth/register`). |
| **Data ownership** | Donors and blood requests are **not** yet tied to `User` in the model; “my requests” / “my donors” APIs are not implemented. |
| **Roles in product** | `User.role` exists in DB; separate dashboards (admin / donor / hospital) and route guards by role are **not** implemented. |
| **Error handling** | Basic `try/catch` on forms; no global 401 handler or unified error format end-to-end. |
| **i18n** | UI is not Arabic/English bilingual yet. |
| **Advanced features** | Geo matching, radius expansion, eligibility rules, QR check-in — documented in `docs/PLANNED_FEATURES_AR.md`, **not** built. |

---

## Documentation (source of truth for “what’s next”)

| File | Purpose |
|------|---------|
| [README.md](README.md) | Quick links, local dev, checklist of improvements |
| [docs/ARCHITECTURE_AND_FEATURES.md](docs/ARCHITECTURE_AND_FEATURES.md) | Layered design, DTOs, worked example (ownership) |
| [docs/PLANNED_FEATURES_AR.md](docs/PLANNED_FEATURES_AR.md) | Arabic roadmap: geo, eligibility, QR, dashboards, i18n |

---

## Suggested priority (from README + architecture docs)

1. Finish **register** flow and polish **auth** (logout everywhere, optional post-login redirect).  
2. **Link** blood requests and donors to the authenticated user + “mine” endpoints.  
3. **Shared layout** + loading/error UX.  
4. **Roles** and separate dashboards if the product requires them.  
5. **Deploy** config (env-based URLs, secrets, CORS).  
6. Longer-term: items in `PLANNED_FEATURES_AR.md`.

---

## How to run (sanity check)

1. Start **MySQL** and align credentials with `Backend/src/main/resources/application.properties`.  
2. Run the **Spring Boot** app (default port **8081**).  
3. In `frontend/`: `npm install` then `npm run dev` (typically **http://localhost:5173**).  
4. Register a user via API or DB if the register page is still stubbed; then login and use Dashboard → Request blood / Add donor.

---

*This status file should be updated when major features ship or scope changes.*
