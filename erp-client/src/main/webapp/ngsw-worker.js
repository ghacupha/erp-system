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
