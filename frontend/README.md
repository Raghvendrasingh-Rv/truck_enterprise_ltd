# Truck Platform Frontend

React frontend for the Truck Booking Platform.

## Stack

- React with Vite
- Tailwind CSS
- React Router
- Axios

## Project Structure

```text
frontend/
  src/
    components/
    hooks/
    layouts/
    pages/
    routes/
    services/
    store/
    utils/
```

## Environment

Create a local `.env` file inside `frontend/` and set:

```env
VITE_API_BASE_URL=http://localhost:8082/api
```

You can copy from:

```text
frontend/.env.example
```

## Install

```bash
cd frontend
npm install
```

## Run

```bash
npm run dev
```

## Build

```bash
npm run build
```

## Deploy To Cloudflare Pages

Use Cloudflare Pages Git integration for the `frontend/` app.

Recommended Pages settings:

- Framework preset: `Vite`
- Root directory: `frontend`
- Build command: `npm run build`
- Build output directory: `dist`

Required environment variable:

```env
VITE_API_BASE_URL=https://<your-backend-domain>/api
```

SPA routing is supported through:

```text
frontend/public/_redirects
```

which contains:

```text
/* /index.html 200
```

This ensures React Router routes like `/customer/bookings` and `/transporter/dashboard` work correctly on direct refresh in Cloudflare Pages.

## App Modes

### Public

- `/`
- `/login`
- `/register`
- `/transporter/login`
- `/transporter/register`

### Customer

- `/customer/search`
- `/customer/results`
- `/customer/bookings`
- `/customer/bookings/:id`
- `/customer/profile`

### Transporter

- `/transporter/dashboard`
- `/transporter/trucks`
- `/transporter/trucks/new`
- `/transporter/trucks/:id/edit`
- `/transporter/bookings`
- `/transporter/bookings/:id`
- `/transporter/profile`

### Admin

- `/admin/cities`
- `/admin/cities/new`

## Auth Flow

- Customer login uses `/api/auth/login`
- Customer register uses `/api/auth/register`
- Transporter login uses `/api/transporter-auth/login`
- Transporter register uses `/api/transporter-auth/register`

JWT is stored in `localStorage` and attached automatically through the Axios client.

## Notes

- Customer search fetches supported cities from `/api/cities`
- Search results call `/api/search`
- Booking flow calls `/api/bookings`
- Transporter truck management uses `/api/trucks`
- Transporter booking actions use `/api/bookings`

## Current Status

The frontend scaffold, routing, layouts, and initial service integration are in place.
You still need Node.js installed locally to run `npm install` and start the app.
