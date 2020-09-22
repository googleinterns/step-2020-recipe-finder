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
import { renderWithRouter } from "../../setupTests";
import { screen, waitFor } from "@testing-library/react";
import Login from "./Login";

test("renders loading before fetching", () => {
  renderWithRouter(<Login />);
  expect(
    screen.getByText("Welcome! Checking if you're logged in ...")
  ).toBeInTheDocument();
});

test("redirects to home page if user is logged in", async () => {
  fetch.once(
    JSON.stringify({ isFirstTime: false, isLoggedIn: true, logUrl: "url" })
  );
  const { history } = renderWithRouter(<Login />);
  await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));
  expect(history.location.pathname).toMatch("/home");
});

test("redirects to sign up page if user is logged in and it's their first time", async () => {
  fetch.once(
    JSON.stringify({ isFirstTime: true, isLoggedIn: true, logUrl: "url" })
  );
  const { history } = renderWithRouter(<Login />);
  await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));
  expect(history.location.pathname).toMatch("/sign-up");
});

test("renders login page if user is not logged in", async () => {
  fetch.once(
    JSON.stringify({ isFirstTime: false, isLoggedIn: false, logUrl: "url" })
  );
  const { history } = renderWithRouter(<Login />);
  await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));
  expect(history.location.pathname).toMatch("/");
});

test("redirects to error page if backend returned error", async () => {
  fetch.once({ status: 500 });
  const { history } = renderWithRouter(<Login />);
  await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));
  expect(history.location.pathname).toMatch("/error");
});
