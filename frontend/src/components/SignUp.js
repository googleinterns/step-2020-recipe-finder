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

import React, { Component } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

class SignUp extends Component {
  render() {
    const dietaryRequirements = [
      "Vegetarian",
      "Vegan",
      "Dairy-Free",
      "Gluten-Free",
      "Halal",
      "Kosher",
      "Nut allergy"
    ];
    return (
      <div>
        <h1>Sign Up</h1>
        <Form action="/api/sign-up" method="POST">
          <Form.Group controlId="formBasicEmail">
            <Form.Label>Name/Nickname</Form.Label>
            <Form.Control
              type="text"
              name="nickname"
              placeholder="Enter your name/nickname"
            />
          </Form.Group>

          <Form.Group controlId="formBasicCheckbox">
            <Form.Label>Dietary Requirements</Form.Label>
            {dietaryRequirements.map((item, index) => (
              <Form.Check
                key={index}
                type="checkbox"
                name="dietaryRequirements"
                value={item}
                label={item}
              />
            ))}
          </Form.Group>
          <Button variant="primary" type="submit">
            Submit
          </Button>
        </Form>
      </div>
    );
  }
}
export default SignUp;
