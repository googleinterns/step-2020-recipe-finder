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
import "./Utilities.css";
import navigatePrevious from "../../icons/navigate_previous.svg";
import Button from "react-bootstrap/Button";
import { getBackground } from "../../utils/Background";

export function loading(text) {
  const message = text === undefined ? "Loading..." : text;
  return (
    <div>
      {getBackground()}
      <div className="spinner-div">
        <div className="spinner-border" role="status">
          <span className="sr-only">Loading...</span>
        </div>
        <h3>{message}</h3>
      </div>
    </div>
  );
}

export function backButton() {
  return (
    <Button variant="" className="back-btn" onClick={goBack}>
      <img src={navigatePrevious} alt="go back to recommendations" />
      Back
    </Button>
  );
}

function goBack() {
  window.history.back();
}
