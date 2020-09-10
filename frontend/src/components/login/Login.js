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
import { Redirect } from "react-router-dom";
import { handleResponseError } from "../utils/APIErrorHandler";
import { errorRedirect } from "../utils/APIErrorHandler";
import { loading } from "../utils/Utilities";

class Login extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      isLoggedIn: false,
      logUrl: "",
      error: null,
      isLoading: true,
      isFirstTime: false,
    };
  }

  componentDidMount() {
    fetch("/api/login-status")
      .then(handleResponseError)
      .then((response) => response.json())
      .then((json) =>
        this.setState({
          isLoggedIn: json.isLoggedIn,
          logUrl: json.logUrl,
          isLoading: false,
          isFirstTime: json.isFirstTime,
        })
      )
      .catch((error) => this.setState({ error: error }));
  }

  render() {
    if (this.state.error !== null) {
      return errorRedirect(this.state.error);
    }

    if (this.state.isLoading) {
      return loading();
    }

    if (this.state.isFirstTime) {
      localStorage.setItem("logOutUrl", this.state.logUrl);
      return <Redirect to="/sign-up" />;
    }

    if (this.state.isLoggedIn) {
      localStorage.setItem("logOutUrl", this.state.logUrl);
      return <Redirect to="/home" />;
    }

    return (
      <div>
        <h1>Recipe Finder</h1>
        <a href={this.state.logUrl}>Login</a>
      </div>
    );
  }
}
export default Login;
