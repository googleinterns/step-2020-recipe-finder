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
import "./SignUp.css";
import Button from "react-bootstrap/Button";
import { getDietaryRequirements } from "../utils/DietaryRequirements";

class SignUp extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      customDiets: [],
    };
    this.addCustomDiet = this.addCustomDiet.bind(this);
    this.removeCustomDiet = this.removeCustomDiet.bind(this);
  }
  render() {
    return (
      <div>
        <h1>Sign Up</h1>
        <Form action="/api/sign-up" method="POST">
          <Form.Group>
            <Form.Label>Name/Nickname</Form.Label>
            <Form.Control
              required
              type="text"
              name="name"
              placeholder="Enter your name/nickname"
            />
          </Form.Group>

          <Form.Group>
            <Form.Label>Dietary Requirements</Form.Label>
            {getDietaryRequirements().map((item, index) => (
              <Form.Check
                key={index}
                type="checkbox"
                name="dietaryRequirements"
                value={item}
                label={item}
              />
            ))}
            {this.state.customDiets.map((item, index) => (
              <div className="custom-diet-div">
                <Form.Control
                  className="custom-diet-input"
                  type="text"
                  name="dietaryRequirements"
                  placeholder="Enter dietary requirement"
                  value={item}
                ></Form.Control>
                <Button
                  className="custom-diet-remove-btn"
                  onClick={() => this.removeCustomDiet(index)}
                  variant="outline-danger"
                >
                  Remove
                </Button>
              </div>
            ))}
            <Button
              className="add-diet-btn w-100"
              variant="outline-primary"
              onClick={this.addCustomDiet}
            >
              Add custom dietary requirement
            </Button>
          </Form.Group>
          <Button className="w-100" variant="primary" type="submit">
            Submit
          </Button>
        </Form>
      </div>
    );
  }

  addCustomDiet() {
    const previousCustomDiets = this.state.customDiets;
    previousCustomDiets.push("");
    this.setState({ customDiets: previousCustomDiets });
  }

  removeCustomDiet(index) {
    const previousCustomDiets = this.state.customDiets;
    previousCustomDiets.splice(index, 1);
    this.setState({ numberCustomDiets: previousCustomDiets });
  }
}
export default SignUp;
