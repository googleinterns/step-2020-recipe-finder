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
import { render, screen } from "@testing-library/react";
import SignUp from "./SignUp";
import { getDietaryRequirements } from "../../../utils/DietaryRequirements";

test("renders 'sign up' page", () => {
  const location = { state: undefined };
  const { getByText } = render(<SignUp location={location} />);
  const title = getByText("Sign Up");
  expect(title).toBeInTheDocument();
});

test("renders 'change account details' page", () => {
  const location = { state: { name: "name", diets: [], allergies: [] } };
  const { getByText } = render(<SignUp location={location} />);
  const title = getByText("Change account details");
  expect(title).toBeInTheDocument();
});

test("displays user diets if 'change account details' page", () => {
  const location = {
    state: { name: "name", diets: ["vegetarian"], allergies: [] },
  };
  render(<SignUp location={location} />);
  const diets = getDietaryRequirements();
  for (let i = 0; i < diets.length; i++) {
    const dietValue = diets[i].value;
    const checkbox = screen.getByDisplayValue(dietValue);
    if (dietValue === "vegetarian") {
      expect(checkbox.checked).toEqual(true);
    } else {
      expect(checkbox.checked).toEqual(false);
    }
  }
});

test("displays user allergies if 'change account details' page", () => {
  const location = {
    state: { name: "name", diets: [], allergies: ["eggs", "nuts"] },
  };
  render(<SignUp location={location} />);
  expect(screen.getByDisplayValue("eggs")).toBeInTheDocument();
  expect(screen.getByDisplayValue("nuts")).toBeInTheDocument();
});
