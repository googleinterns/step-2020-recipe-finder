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
import AccountHeader from "../account-header/AccountHeader";
import Button from "react-bootstrap/Button";
import { Link } from "react-router-dom";
import { getDietaryRequirements } from "../../../utils/DietaryRequirements";
import { handleResponseError } from "../../utils/APIErrorHandler";
import { errorRedirect } from "../../utils/APIErrorHandler";
import { loading } from "../../utils/Utilities";

class Account extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      name: "",
      diets: [],
      customDiets: [],
      isLoading: true,
      error: null,
    };
  }

  componentDidMount() {
    fetch("/api/account")
      .then(handleResponseError)
      .then((response) => response.json())
      .then((json) =>
        this.setState({
          name: json.name,
          diets: json.diets,
          customDiets: json.customDiets,
          isLoading: false,
        })
      )
      .catch((error) => this.setState({ error: error }));
  }

  render() {
    if (this.state.error !== null) {
      return errorRedirect(this.state.error);
    }

    if (this.state.isLoading) {
      return loading("Getting account details ...");
    }

    return (
      <div>
        <AccountHeader />
        <h1>My Account</h1>
        <h4>{this.state.name}</h4>
        <h3>My dietary requirements:</h3>
        <ul>
          {this.state.diets.map((item, index) => (
            <li key={index}>{this.getLabelForDiet(item)}</li>
          ))}
          {this.state.customDiets.map((item, index) => (
            <li key={index}>{item}</li>
          ))}
        </ul>
        <Link
          to={{
            pathname: "/sign-up",
            state: {
              name: this.state.name,
              diets: this.state.diets,
              customDiets: this.state.customDiets,
            },
          }}
        >
          <Button>Change account details</Button>
        </Link>
      </div>
    );
  }

  getLabelForDiet(diet) {
    return getDietaryRequirements().filter((item) => item.value === diet)[0]
      .label;
  }
}
export default Account;
