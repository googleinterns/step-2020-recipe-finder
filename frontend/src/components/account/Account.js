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
import AccountHeader from "./AccountHeader";

class Account extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      name: "Eleanor",
      dietaryRequirements: ["Vegetarian", "Nut free"],
    };
  }
  render() {
    return (
      <div>
        <AccountHeader />
        <h1>My account</h1>
        <h4>{this.state.name}</h4>
        <h3>My dietary requirements:</h3>
        <ul>
          {this.state.dietaryRequirements.map((item, index) => (
            <li key={index}>{item}</li>
          ))}
        </ul>
      </div>
    );
  }
}
export default Account;
