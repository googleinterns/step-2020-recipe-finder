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

import Button from "react-bootstrap/Button";
import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import { handleResponseError } from "../utils/APIErrorHandler";
import { errorRedirect } from "../utils/APIErrorHandler";
import { loading } from "../utils/Utilities";
import "./Login.css";
import { getRandomBackground } from "../../utils/Background";
import Walkthrough from "./Walkthrough";

class Login extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      loggedIn: false,
      logUrl: "",
      error: null,
      showModal: false,
      background: getRandomBackground(),
      loading: true,
      firstTime: false,
    };

    this.handleClose = this.handleClose.bind(this);
    this.setShowModal = this.setShowModal.bind(this);
  }

  componentDidMount() {
    fetch("/api/login-status")
      .then(handleResponseError)
      .then((response) => response.json())
      .then((json) => {
        this.setState({
          loggedIn: json.isLoggedIn,
          logUrl: json.logUrl,
          loading: false,
          firstTime: json.isFirstTime,
        });
      })
      .catch((error) => this.setState({ error: error }));
  }

  render() {
    if (this.state.error !== null) {
      return errorRedirect(this.state.error);
    }

    if (this.state.loading) {
      return loading("Welcome! Checking if you're logged in ...", true);
    }

    if (this.state.firstTime) {
      this.trySavingSignOutToSessionStorage();
      return <Redirect to="/sign-up" />;
    }

    if (this.state.loggedIn) {
      this.trySavingSignOutToSessionStorage();
      return <Redirect to="/home" />;
    }

    return (
      <div id="login-div">
        <Walkthrough
          handleClose={this.handleClose}
          showModal={this.state.showModal}
        />
        {this.state.background}
        <h1 className="white-text text-center">Recipe Finder</h1>
        <div className="login-div">
          <Button
            variant="secondary"
            onClick={this.setShowModal}
            className="walkthrough-btn"
          >
            Show me how Recipe Finder works
          </Button>
          <a href={this.state.logUrl}>
            <Button className="login-btn">Login</Button>
          </a>
        </div>
      </div>
    );
  }

  handleClose() {
    this.setState({ showModal: false });
  }

  setShowModal() {
    this.setState({ showModal: true });
  }

  trySavingSignOutToSessionStorage() {
    try {
      sessionStorage.setItem("signOutUrl", this.state.logUrl);
    } catch (error) {
      console.log(error);
    }
  }
}
export default Login;
