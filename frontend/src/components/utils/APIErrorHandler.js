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
import { backButton } from "./Utilities";

export function handleResponseError(response) {
  if (!response.ok) {
    throw Error(response.statusText);
  }
  return response;
}

export function errorRedirect(error) {
  if (error && error.message === "Failed to fetch") {
    return <Redirect to="/offline" />;
  }
  return <Redirect to={{ pathname: "/error", state: { error: error } }} />;
}

class ErrorPage extends Component {
  render() {
    const error = this.props.location.state.error;
    return (
      <div>
        {backButton()}
        <h1>Error</h1>
        <p>{error && error.message}</p>
      </div>
    );
  }
}
export default ErrorPage;
