# Training Management Portal - Frontend

This frontend is a React + Vite application built to consume the existing Spring Boot backend APIs.

## Tech stack

- React 18
- Vite
- TypeScript
- Material UI
- React Router
- Axios
- React Hook Form
- Yup validation
- React Query

## Setup

1. Copy example environment file:

   ```bash
   cd frontend
   cp .env.example .env
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Start the frontend:

   ```bash
   npm run dev
   ```

4. Open the app in your browser at the address shown by Vite (default `http://localhost:4173`).

## Backend connection

The frontend reads `VITE_API_URL` from `.env`. By default it points to:

```text
VITE_API_URL=http://localhost:8080/api/v1
```

If the backend runs on a different port or host, update `.env` accordingly.

## Notes

- Axios attaches `Authorization: Bearer <token>` automatically when a token is stored in `localStorage`.
- Pages include dashboard, employees, trainers, training programs, assignments, profile, and error routes.
- Login expects an auth endpoint at `/api/v1/auth/login` returning `{ token: string }`.

## Run backend and database

From the repo root:

```bash
docker compose up -d
cd backend
./gradlew bootRun
```

Then run the frontend from `frontend/`:

```bash
npm install
npm run dev
```
