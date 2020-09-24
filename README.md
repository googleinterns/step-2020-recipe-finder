# Recipe Finder
This repo contains the 2020 STEP Internship Project of [Azhara](https://github.com/azhara-a) and [Mohamed](https://github.com/mam249).

This application allows users to input ingredients and be given a selection of recipes to follow. 

## Technologies
  - Google AppEngine: testing and deployment
  - Java Servlets: backend
  - React.js: frontend
  - Google Cloud Text-To-Speech API: reading recipes aloud
  - Datastore API: storing user data and recipes
  - Google User Service API: authenticating and identifying users based on their google accounts
  - Google Custom Search JSON API: receiving recipe links from BBCGoodFood
  - JSoup: scraping recipes from webpages

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
 
## Licensing
 Licensed under the Apache License, Version 2.0
