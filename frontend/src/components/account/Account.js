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
import Button from "react-bootstrap/Button";
import { Link } from "react-router-dom";
import { handleResponseError } from "../utils/GeneralErrorHandler";
import { errorRedirect } from "../utils/GeneralErrorHandler";
import { loading } from "../utils/Utilities";

class Account extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      name: "",
      dietaryRequirements: [],
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
          dietaryRequirements: json.dietaryRequirements,
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
          {this.state.dietaryRequirements.map((item, index) => (
            <li key={index}>{item}</li>
          ))}
        </ul>
        <Link to="/dietary">
          <Button>Change Dietary requirements</Button>
        </Link>
      </div>
    );
  }
}
export default Account;
