# Recipe Finder
This repo contains the 2020 STEP Internship Project of [Azhara](https://github.com/azhara-a) and [Mohamed](https://github.com/mam249).

Recipe Finder is a progressive web app that allows users to input ingredients and be given a selection of recipes to follow. 

## Technologies
  - Google AppEngine: testing and deployment
  - Java Servlets: backend
  - React.js: frontend
  - Google Cloud Text-To-Speech API: reading recipes aloud
  - Datastore API: storing user data and recipes
  - Google User Service API: authenticating and identifying users based on their google accounts
  - Google Custom Search JSON API: receiving recipe links from BBCGoodFood
  - JSoup: scraping recipes from webpages
  - Workbox plugin: precaching and caching files for offline functionality
  - React-app-rewired: recongifuring React's default service worker with our custom service worker

## Prerequisites
  - Google Cloud AppEngine Project
  - Maven
  - Google API Authentication
  - Node.js

## Testing
  - Backend: `cd backend; mvn package appengine:run`
  - Frontend: `cd frontend; npm install; npm start`
  
## Deployment
  - Backend: `cd backend; mvn package appengine:deploy`
  - Frontend: `cd frontend; npm install; npm run build; gcloud app deploy --project=YOUR_PROJECT_ID`
  - Make sure to run dispach.yaml file after deploying backend and frontend to enable correct routing: `gcloud app deploy dispatch.yaml`
  - Backend can be accessed via `/api/` fetch requests

## Progressive Web App
  - Make sure to run the production build to enable offline functionality (`npm run build`)
  - Workbox Strategies for different fetch requests:
    - `/api/favourites` - NetworkFirst (it will only be cached once the user visits their favourite recipes) User can view their favourite recipes when they're offline
    - all other `/api` backend requests - NetworkOnly
    - all other requests - StaleWhileRevalidate, fetching from cache and network in parallel for faster response

## Licensing
 Licensed under the Apache License, Version 2.0
