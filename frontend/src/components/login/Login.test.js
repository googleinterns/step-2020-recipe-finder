// Copyright 2020 Google LLC

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     https://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import React from "react";
import { rest } from "msw";
import { renderWithRouter } from "../../setupTests";
import { screen } from "@testing-library/react";
import App from "../../App";

test("renders error page if backend returned error", async () => {
  fetch.once({ status: 500 });
  renderWithRouter(<App />);
  expect(await screen.findByText("Error")).toBeInTheDocument();
});

test("renders home page if user is logged in", async () => {
  fetch.once(
    JSON.stringify({ isFirstTime: false, isLoggedIn: true, logUrl: "url" })
  );
  renderWithRouter(<App />);
  expect(await screen.findByText("Input Ingredients")).toBeInTheDocument();
});

test("renders sign up page if user is logged in and this their first time", async () => {
  fetch.once(
    JSON.stringify({ isFirstTime: true, isLoggedIn: true, logUrl: "url" })
  );
  renderWithRouter(<App />);
  expect(await screen.findByText("Sign Up")).toBeInTheDocument();
});

test("renders login page if user is not logged in", async () => {
  fetch.once(
    JSON.stringify({ isFirstTime: false, isLoggedIn: false, logUrl: "url" })
  );
  renderWithRouter(<App />);
  expect(await screen.findByText("Login")).toBeInTheDocument();
});
