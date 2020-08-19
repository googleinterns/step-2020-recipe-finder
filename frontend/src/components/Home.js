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
import './Home.css';

class Home extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      isLoggedIn: false,
      logUrl: "",
    };
  }

  componentDidMount() {
    fetch("/api/login-status")
      .then((response) => response.json())
      .then((json) =>
        this.setState({ isLoggedIn: json.isLoggedIn, logUrl: json.logUrl })
      );
  }

  render() {
    return (
      <div>
        <h1>Recipe Finder</h1>
        <a href={this.state.logUrl} className="home-buttons">
          {this.getLoginOrLogoutLabel()}
        </a>
        <Link to="/text">
          <Button className="home-buttons">Input Ingredients</Button>
        </Link>
      </div>
    );
  }

  getLoginOrLogoutLabel() {
    return this.state.isLoggedIn ? "Logout" : "Login";
  }
}
export default Home;
