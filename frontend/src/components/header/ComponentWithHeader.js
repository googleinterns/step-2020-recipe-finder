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
import {errorRedirect} from "../utils/APIErrorHandler";

class ComponentWithHeader extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      error: null,
    };
  }
  render() {
    if (this.state.error !== null) {
      return errorRedirect(this.state.error);
    }
    return (
      <div>
        <AccountHeader />
        {this.renderContent()}
      </div>
    );
  }
}
export default ComponentWithHeader;
