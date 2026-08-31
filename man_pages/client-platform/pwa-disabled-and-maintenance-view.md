# Client platform — PWA disabled, maintenance view, dockerized test CI

## Why

A reworked Asset Registration detail page was deployed but users kept seeing the
**old** layout, and every hard refresh logged them out. Root cause:

- `angular.json` production config had `serviceWorker: true` with `ngsw-config.json`
  set to **prefetch `/index.html` and every `/*.js`**. So each production image
  shipped an Angular PWA service worker that served the whole app shell offline-first.
- A new deployment is therefore invisible until the SW completes an update cycle, and
  any mismatch (e.g. a build that does not emit `ngsw.json`) leaves the old SW serving
  a stale cached app indefinitely, with reload loops that drop the session.

For an always-online internal ERP the offline capability is worth little and the
stale-deploy failure mode is expensive, so the PWA was removed.

## What changed

### PWA / service worker disabled
- `angular.json` – production configuration: `serviceWorker: false` (removed
  `ngswConfigPath`). No `ngsw-worker.js` / `ngsw.json` is generated any more.
- `app/app.module.ts` – `ServiceWorkerModule.register('ngsw-worker.js', { enabled: false })`.
- `src/main/webapp/ngsw-worker.js` – **new kill-switch worker**, registered as a build
  asset so it is served at `/ngsw-worker.js`. Browsers that still have the old caching
  worker re-fetch this script on their next navigation; it calls
  `registration.unregister()`, purges every Cache Storage bucket and reloads open
  tabs, so existing clients self-heal to the fresh build. Keep this file in place
  permanently.
- Production builds now use content-hashed filenames (`outputHashing: all`, already
  set), so the browser HTTP cache can no longer serve a stale stably-named bundle.

Manual recovery for a still-stuck browser: DevTools → Application → Service Workers →
Unregister (or Clear site data), then reload.

### Maintenance view (server-not-live)
- `app/layouts/maintenance/maintenance.component.{ts,html,scss}` + `maintenance.route.ts`
  (`path: 'maintenance'`), wired into `app-routing.module.ts` `LAYOUT_ROUTES` and
  declared in `AppModule`. Full-page "The ERP system is temporarily unavailable" card
  with a wrench icon, a countdown, a **Retry now** button, and an automatic poll of
  `management/health/readiness` every 10s that navigates the user back to the page they
  were on once the server answers.
- `app/core/interceptor/server-down.interceptor.ts` – registered right after
  `AuthInterceptor`. When an `/api` `/services` `/management` call fails with a
  "server down" status (`0`, `502`, `503`, `504`) it fires **one confirmation probe**
  against `management/health/readiness` (through `HttpBackend`, bypassing the
  interceptor chain, 4s timeout). Only if that probe also fails does it store the
  current URL and navigate to `/maintenance`. The confirmation step stops aborted
  requests (user navigating away → status 0) from bouncing people to the maintenance
  page.

### Dockerized client test CI
- `.github/workflows/client-ci.yml` – new `client-tests` job that runs
  `npm install` + `npm run jest` inside `container: node:14.21.3`, because the client
  is pinned to Node 14 (`node-sass@6` / `jest-preset-angular` do not run on newer
  Node). The existing `pipeline` job now `needs: client-tests`, so a failing suite
  blocks the image build/push.

### Build-time reduction (initial)
- `webpack/webpack.custom.js` – `webpack-bundle-analyzer` (which re-parses every emitted
  chunk and writes `target/stats.html` on *every* production build) is now opt-in via
  `ANALYZE=true`. Measured effect: production `ng build` dropped from ~137s to ~93s
  webpack time on a warm filesystem cache. See
  `man_pages/client-platform/build-performance-investigation.md` for the deeper work.

## Verification

- `npm run lint` / `npm run prettier:check` clean for the changed files.
- `ng build --configuration production` succeeds; `target/classes/static` contains
  hashed bundles, the kill-switch `ngsw-worker.js`, and **no** `ngsw.json`.
- Deployed to `ghacupha/erp-client:1.7.9` (previous image kept as `:1.7.9-orig`) and
  recreated via `docker-compose -f docker-compose.yml up -d --no-deps --force-recreate
  erp-client-web`. Confirmed in-browser: `/maintenance` renders and auto-recovers; the
  home page loads without a false maintenance redirect.
- Jest suite runs in CI only (local Node 22 cannot run `node-sass@6`).
