# Truck Bazaar

Monorepo for the Truck Bazaar platform.

## Structure

```text
Truck/
  backend/   Spring Boot API
  frontend/  React + Vite web app
```

## Local Verification Before Push

Run these checks before pushing code to GitHub:

```bash
cd backend
./gradlew test

cd ../frontend
npm run lint
npm run build
```

## Ideal Git Push Flow

1. Check current changes:

```bash
git status
```

2. Verify backend and frontend locally:

```bash
cd backend && ./gradlew test
cd ../frontend && npm run lint && npm run build
```

3. Stage the intended files:

```bash
cd ..
git add -A
```

4. Review staged changes:

```bash
git status
git diff --cached --stat
```

5. Commit with a meaningful message:

```bash
git commit -m "Add CI/CD setup and frontend improvements"
```

6. Push to GitHub:

```bash
git push origin master
```

## Important Notes

- Do not commit `.env` files.
- Commit `backend/gradle/wrapper/gradle-wrapper.jar` so Gradle CI works.
- Commit `frontend/package-lock.json` after dependency changes so `npm ci` works in CI.
- Do not commit generated folders such as:
  - `backend/build`
  - `backend/.gradle`
  - `frontend/node_modules`
  - `frontend/dist`

## Deployment

- Frontend: Cloudflare Pages
- Backend: Render using Docker

See module-specific setup details in:

- [backend/README.md](/Users/raghvendra/Projects/Truck/backend/README.md)
- [frontend/README.md](/Users/raghvendra/Projects/Truck/frontend/README.md)
