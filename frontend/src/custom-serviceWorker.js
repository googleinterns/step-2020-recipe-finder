/* eslint-disable no-undef */
if (workbox) {
  console.log(`Workbox is loaded 🎉`);
} else {
  console.log(`Workbox didn't load `);
}
// eslint-disable-next-line
workbox.precaching.precacheAndRoute(self.__precacheManifest);
// eslint-disable-next-line
self.addEventListener("install", (event) =>
  event.waitUntil(self.skipWaiting())
);
// eslint-disable-next-line
self.addEventListener("activate", (event) =>
  event.waitUntil(self.clients.claim())
);

//   self.addEventListener('fetch', (event) => {
//     const {request} = event;
//     const url = new URL(request.url);

//     if (url.origin === location.origin && url.pathname === '/') {
//       event.respondWith(new workbox.strategies.NetworkFirst().handle({event, request}));
//     }
//   });

//   self.addEventListener('fetch', function(event) {
//     event.respondWith(
//       fetch(event.request).catch(function() {
//         return caches.match(event.request);
//       })
//     );
//   });

//   event.respondWith(
//     caches.match(event.request)
//     .then(response => {
//       if (response) {
//         console.log('Found ', event.request.url, ' in cache');
//         return response;
//       }
//       console.log('Network request for ', event.request.url);
//       return fetch(event.request)
//       .then(response => {
//         if (response.status === 404) {
//           return caches.match('pages/404.html');
//         }
//         return caches.open(staticCacheName)
//         .then(cache => {
//           cache.put(event.request.url, response.clone());
//           return response;
//         });
//       });
//     }).catch(error => {
//       console.log('Error, ', error);
//       return caches.match('pages/offline.html');
//     })
//   );

const FALLBACK_URL = "/offline";

const urlHandler = new workbox.strategies.networkFirst();

workbox.routing.registerRoute("/", ({ event }) => {
  return urlHandler
    .handle({ event })
    .then((response) => {
      return response || caches.match(FALLBACK_URL);
    })
    .catch(() => caches.match(FALLBACK_URL));
});
