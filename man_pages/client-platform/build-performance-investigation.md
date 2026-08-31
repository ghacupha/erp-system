# Client build performance — investigation

Goal: bring the `erp-client` production build (`npm run webapp:build:prod` /
`ng build --configuration production`) to **≤ 2 minutes**.

## Measurements so far (host, Node 22 + `--openssl-legacy-provider`, warm FS cache)

| Build | webpack `Time:` | wall (`real`) |
|---|---|---|
| prod, bundle-analyzer ON (as it shipped), cold-ish cache | ~137 s | — |
| prod, bundle-analyzer gated OFF, warm cache | **93.3 s** | 1m44s |

`config.cache = { type: 'filesystem', cacheDirectory: target/webpack }` is already
configured in `webpack/webpack.custom.js`, so warm builds are markedly faster than the
first build after a `webpack.custom.js` / `angular.json` / `tsconfig` change (those are
declared `buildDependencies`, so editing them invalidates the whole cache).

## Applied — results

| build | webpack `Time:` |
|---|---|
| prod, as it shipped (analyzer on) | ~137 s |
| + analyzer gated off, warm cache | ~93 s |
| + chart.js lazy-loaded + devtools/prod-mode fix, warm cache | **~78 s** ✅ under 2 min |

Initial JS bundle: `main.js` **1.64 MB → 191 bytes**; initial transfer (JS + CSS, gz)
≈ 203 KB. `chart.js` (229 KB) + `ng2-charts` now load only with the home/dashboard
route.

1. **`webpack-bundle-analyzer` made opt-in** (`webpack/webpack.custom.js`, gated on
   `ANALYZE=true`). Cold cache: adds real time. Warm cache: ~4 s. Also stops
   `target/stats.html` churn on every build.

2. **`chart.js` / `ng2-charts` moved out of the initial bundle.** `HomeModule` (which
   imported `PrepaymentsDashboardModule` → `NgChartsModule` → `chart.js`) was eagerly
   imported by `AppModule`. Changed to a lazy route:
   `{ path: '', loadChildren: () => import('./home/home.module') }` in
   `app-routing.module.ts`, and removed `HomeModule` from `AppModule` imports. chart.js
   now lives in the home lazy chunk only. This is the change that put the initial
   bundle on a diet and the build under 2 minutes.

3. **`__DEBUG_INFO_ENABLED__` is now `false` for production builds**
   (`webpack/webpack.custom.js`). `webpack/environment.js` hard-codes it `true`, so the
   old define (`environment.__DEBUG_INFO_ENABLED__ || config.mode === 'development'`)
   was **always true**, which meant a production build:
   - never called `enableProdMode()` (`bootstrap.ts` gates on `!DEBUG_INFO_ENABLED`) —
     Angular ran in development mode in prod;
   - left router `enableTracing` on;
   - bundled `@ngrx/store-devtools`.
   With the fix, prod builds get `DEBUG_INFO_ENABLED === false`: `enableProdMode()` runs
   (verified — no "Angular is running in development mode" console line), router tracing
   is off (`enableTracing:!1` in the bundle), and
   `StoreDevtoolsModule.instrument({ name: 'ERP App States', … })` is dead-code
   eliminated (the `name` string no longer appears in the bundle). Dev builds are
   unchanged.

### Not changed / partially done

- **`@ngrx/store-devtools` residual code** (~17 KB): the `.instrument()` call is gone,
  but Terser did not prune the library module itself (still referenced from the
  `...(DEBUG_INFO_ENABLED ? [StoreDevtoolsModule] : [])` in `imports`/`exports`). It no
  longer runs or connects; removing the last 17 KB needs the `import` moved into a
  dev-only sub-module. Low priority.
- **Font Awesome deep imports** — investigated, not changed. `config/font-awesome-icons.ts`
  already uses named imports and `@fortawesome/free-solid-svg-icons` sets
  `sideEffects: false`, so tree-shaking already works: the ~34 KB in the bundle is the
  ~60 icons actually referenced, not the ~1.3 MB barrel. Per-icon deep imports would
  produce the same bytes.

## Findings — the initial bundle (`ANALYZE=true` run + `target/classes/target/stats.html`)

The chunk called `796.*.js` in `--verbose` output is **the eager/initial app bundle**
(`bootstrap.ts + 189 modules concatenated`), not a lazy route. Parsed size **1.64 MB
(383 KB gzip)** — roughly 6× the next-largest chunk. Terser minifies this one unit
serially, so shrinking it speeds the build *and* first load. Top contents:

| ~parsed | module | action |
|---|---|---|
| 188 KB | **`chart.js`** (`dist/chart.mjs` + `helpers.segment`) | eagerly bundled — lazy-load it; only pull it in on screens that render charts |
| 90 KB | `@ng-bootstrap/ng-bootstrap` | needed app-wide; leave |
| 74 KB | `@angular/animations` browser | leave |
| 61 KB | `@ng-select/ng-select` | used app-wide in forms; leave |
| 34 KB | **`@fortawesome/free-solid-svg-icons` full index** | `config/font-awesome-icons.ts` curates a list but importing the pack index pulls everything — import the individual `faXxx` icons instead |
| 17 KB | **`@ngrx/store-devtools`** | **shipped in the production bundle** — wrap `StoreDevtoolsModule.instrument(...)` in `!environment.production` (or drop from prod imports) |
| 17 KB | `@ngrx/store` core | leave |

Secondary: `4711.*.js` 0.27 MB parsed, `common.*.js` 0.11 MB. ~200 lazy chunks total
(JHipster emits one per entity route) — hurts first-load waterfall, not build time.
`styles` CSS 196 KB (Bootstrap + Bootswatch) — minor.

## Next actions (remaining)

1. **CI**: the slow part of the `pipeline` job is uncached `npm install` (~5 min). Add
   `actions/cache` on `~/.npm` keyed by `package-lock.json`, and cache `target/webpack`
   (the webpack FS cache) keyed by lockfile + `webpack.custom.js` hash. This is what
   still stands between the *pipeline* and a ~3 min end-to-end CI run.
2. Cold builds (no `target/webpack`) are still ~130–140 s. The FS cache is only reused
   when `webpack.custom.js` / `angular.json` / `tsconfig*` are unchanged. Keep churn in
   those files low; in CI, restore the cache.
3. Optional: `speed-measure-webpack-plugin` behind `MEASURE=true` for per-plugin timing
   (needs the dev dep installed under Node 14).
4. Optional: drop the last ~17 KB of `@ngrx/store-devtools` by moving its `import` into a
   dev-only module.
5. `4711.js` (275 KB) and `1147.js` (229 KB, now the chart/dashboard chunk) are the next
   largest lazy chunks if further trimming is wanted — not on any critical path.

## Housekeeping

`--stats-json` writes a ~950 MB `stats.json` into `target/classes/static/` (it would be
copied into the nginx image). Delete it after any stats run; do not use `--stats-json`
for routine builds.
