/*
 * Erp System - Mark X No 12 (Kadar Series) Client 1.8.0
 * Copyright © 2021 - 2026 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Kill-switch service worker.
 *
 * The Angular PWA service worker was disabled for this client (angular.json
 * `serviceWorker: false`, AppModule `ServiceWorkerModule.register(..., { enabled: false })`).
 * Browsers that already registered the previous, caching service worker will
 * re-fetch this script on their next navigation. This replacement unregisters
 * itself, purges every Cache Storage bucket it left behind and reloads open
 * tabs, so the client stops serving a stale, cached build.
 *
 * Keep this file in place indefinitely: removing it would let old clients keep
 * their stale service worker forever.
 */
self.addEventListener('install', () => {
  self.skipWaiting();
});

self.addEventListener('activate', event => {
  event.waitUntil(
    (async () => {
      try {
        const keys = await caches.keys();
        await Promise.all(keys.map(key => caches.delete(key)));
      } catch (e) {
        // ignore - best effort cache purge
      }
      try {
        await self.registration.unregister();
      } catch (e) {
        // ignore
      }
      const clients = await self.clients.matchAll({ type: 'window' });
      clients.forEach(client => client.navigate(client.url));
    })()
  );
});

// No-op fetch handler: never serve anything from cache.
self.addEventListener('fetch', () => {});
