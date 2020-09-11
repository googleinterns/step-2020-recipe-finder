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
import sign_out from "../../../icons/sign_out.svg";
import "./Account.css";

class Account extends Component {
  constructor(properties) {
    super(properties);
    const signOut = sessionStorage.getItem("signOutUrl");
    this.state = {
      name: "",
      diets: [],
      allergies: [],
      isLoading: true,
      error: null,
      signOut: signOut !== null ? signOut: this.fetchSignOut()
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
          allergies: json.allergies,
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
        <div className="centered-container">
          <div className="my-account-header">
            <div className="account-title">
              <h1 className="account-page-title">My Account</h1>
            </div>
            <div className="sign-out-div">
              <a href={this.state.signOut} onClick={this.removeSignOut}>
                <img src={sign_out} alt="account" id="account-icon" />
                <div id="sign-out-text">Sign Out</div>
              </a>
            </div>
          </div>
          <h3>My name/nickname:</h3>
          <h4>{this.state.name}</h4>
          <h3>My dietary requirements:</h3>
          <p>{this.getMessageIfNoDiet()}</p>
          <ul>
            {this.state.diets.map((item, index) => (
              <li key={index}>{this.getLabelForDiet(item)}</li>
            ))}
          </ul>
          <h3>Allergies/Food I can't eat:</h3>
          <p>{this.getMessageIfNoAllergies()}</p>
          <ul>
            {this.state.allergies.map((item, index) => (
              <li key={index}>{item}</li>
            ))}
          </ul>
          <Link
            to={{
              pathname: "/sign-up",
              state: {
                name: this.state.name,
                diets: this.state.diets,
                allergies: this.state.allergies,
              },
            }}
          >
            <Button>Change account details</Button>
          </Link>
        </div>
      </div>
    );
  }

  fetchSignOut() {
    fetch("/api/login-status")
      .then(handleResponseError)
      .then((response) => response.json())
      .then((json) => {
        return json.logUrl;
      })
      .catch((error) => this.setState({ error: error }));
  }

  removeSignOut() {
    sessionStorage.removeItem("signOutUrl");
  }

  getMessageIfNoDiet() {
    if (this.state.diets.length === 0) {
      return "No dietary requirements";
    }
  }

  getMessageIfNoAllergies() {
    if (this.state.allergies.length === 0) {
      return "No allergies/food I can't eat";
    }
  }
  
  getLabelForDiet(diet) {
    return getDietaryRequirements().filter((item) => item.value === diet)[0]
      .label;
  }
}
export default Account;
