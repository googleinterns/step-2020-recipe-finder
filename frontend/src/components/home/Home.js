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
import { Link } from "react-router-dom";
import account from "../../icons/account.svg";
import "./Home.css";
import { getBackground } from "../../utils/Background";

class Home extends Component {
  render() {
    return (
      <div>
        {getBackground()}
        <div className="home-header">
          <h1 id="home-title" className="white-text">
            Recipe Finder
          </h1>
          <div className="account-div">
            <Link to="/account">
              <img src={account} alt="account" id="account-icon" />
              <div id="account-text">Account</div>
            </Link>
          </div>
        </div>
        <div className="input-div">
          <Link to="/text">
            <Button id="input-btn">Input Ingredients</Button>
          </Link>
        </div>
      </div>
    );
  }
}
export default Home;
