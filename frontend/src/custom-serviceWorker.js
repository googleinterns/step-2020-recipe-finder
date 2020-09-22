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

workbox.routing.registerRoute(
  ({ url }) =>
    url.origin === "https://fonts.googleapis.com" ||
    url.origin === "https://fonts.gstatic.com",
  new workbox.strategies.StaleWhileRevalidate({
    cacheName: "google-fonts",
  })
);

workbox.routing.registerRoute(
  ({ url }) => url.pathname.startsWith("/api/"),
  new workbox.strategies.NetworkOnly()
);

workbox.routing.registerRoute(
  "/",
  new workbox.strategies.StaleWhileRevalidate()
);

workbox.routing.setCatchHandler(({ url, event, params }) => {
  console.log("In catch handler");
  console.log(url);
  console.log(event);
  console.log(params);
});

workbox.routing.setDefaultHandler(({ url, event, params }) => {
  console.log("In default handler");
  console.log(url);
  console.log(event);
  console.log(params);
});
