# BloodBridge Backend (Bridging)

Spring Boot API for linking donors, blood requests, donations, and notifications. This README summarizes what was implemented relative to the BloodBridge domain plan.

## Base URL

- **Context path:** `/api` (set in `application.properties` via `server.servlet.context-path=/api`)
- **Example:** `http://localhost:8081/api/auth/login`

The React (or any) client must call paths under `/api/...`, not the root.

## Tech stack

- Spring Boot 4, Spring Security (JWT), JPA/Hibernate, MySQL

## Domain model (high level)

| Area | Notes |
|------|--------|
| **User** | `name`, `email`, `password`, `phone`, `role` (`ADMIN`, `DONOR`, `REQUESTER`), `createdAt` (`Instant`). Password is hidden from JSON (`@JsonIgnore`). |
| **Donor** | One-to-one with `User`; `bloodType`, `location`, `available`, `lastDonationDate`. |
| **BloodRequest** | Requester = `User`; `bloodType`, `hospitalName` (DB column `hospital`), `location`, `quantity`, `status` (`PENDING`, `APPROVED`, `COMPLETED`, `CANCELLED`). |
| **Donation** | Links `Donor` + `BloodRequest`; `donationDate`, `status` (`MATCHED`, `DONATED`). |
| **Notification** | `user`, `message`, `read`, `createdAt`. |
| **Hospital** | Standalone entity (optional future linking to requests). |

## Main HTTP endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/register` | Register (`email`, `password`, optional `name`, `phone`, `role`: `DONOR` or `REQUESTER` only; `ADMIN` blocked). |
| POST | `/api/auth/login` | Returns JWT + email in `AuthResponse`. |
| POST | `/api/donors` | Create/update donor profile for current user. |
| GET | `/api/donors/my-profile` | Current user’s donor row(s). |
| GET | `/api/donors/filter` | Query: `bloodType`, optional `location`, optional `available`. |
| GET | `/api/donors/available`, `/api/donors/blood-type/{type}` | Listing helpers. |
| POST | `/api/requests` | Create blood request (body: `bloodType`, `hospitalName` or legacy `hospital`, `location`, `quantity`). Triggers matching notifications. |
| GET | `/api/requests`, `/api/requests/my-requests`, `/api/requests/status/{status}`, `/api/requests/{id}` | Lists / detail (detail: owner or admin). |
| POST | `/api/requests/{id}/match` | Re-run donor matching + notifications. |
| POST | `/api/donations/accept` | Body `{ "requestId": ... }`; donor must be eligible (≥3 months since last donation). |
| POST | `/api/donations/{id}/complete` | Marks donation done; completes request; updates donor dates/availability. |
| GET | `/api/notifications` | Current user’s notifications. |
| PATCH | `/api/notifications/{id}/read` | Mark as read. |
| GET | `/api/admin/users` | **Admin only.** |
| PATCH | `/api/admin/requests/{id}/status` | **Admin only.** Body: `{ "status": "APPROVED" \| "CANCELLED" \| ... }`. |

Authenticated routes expect header: `Authorization: Bearer <token>`.

## Security

- `@EnableMethodSecurity` is enabled; `@PreAuthorize` on services (e.g. admin-only lists) is active.
- Public: `/api/auth/**`, `/error`, `OPTIONS /**`.
- Role names in JWT/Spring: `ROLE_ADMIN`, `ROLE_DONOR`, `ROLE_REQUESTER`.

## Utilities

- **`BloodTypeParser`** — accepts enum names (`O_NEGATIVE`) or short forms (`O-`, `A+`, …).
- **`DonationEligibility`** — enforces minimum **3 months** between donations before accepting a new match.

## Tests

- Unit tests: `BloodTypeParserTest`, `DonationEligibilityTest`.
- `BridgingApplicationTests` loads the full Spring context (needs MySQL as configured).

## Database migration hints (existing MySQL data)

If you upgraded from an older schema:

```sql
UPDATE users SET role = 'DONOR' WHERE role = 'USER';
UPDATE blood_requests SET status = 'CANCELLED' WHERE status = 'CANCELED';
UPDATE users SET created_at = UTC_TIMESTAMP(6) WHERE created_at IS NULL;
```

Adjust table/column names if yours differ. Hibernate `ddl-auto=update` will add new columns/tables; enum string values must match the Java enums.

## Not in this phase (per plan)

- Real-time push (WebSocket/STOMP) for notifications — REST only for now.
- Flyway/Liquibase — schema still driven by JPA `ddl-auto` unless you add migrations later.

## Run

```bash
mvn spring-boot:run
```

Ensure MySQL is running and `application.properties` points to the correct database.
