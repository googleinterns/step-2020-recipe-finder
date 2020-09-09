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
import { Recipe } from "../Recipe";
import "./Favourites.css";

class History extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      recipes: [],
    };
  }

  componentDidMount() {
    fetch("/api/history")
      .then((response) => response.json())
      .then((json) => this.setState({ recipes: json }))
      .catch((err) => console.log(err));
  }

  render() {
    return (
      <div>
        <AccountHeader />
        <h1>History</h1>
        {this.state.recipes.map((recipe, index) => {
          const button = (
            <div className="right-side-btn">
              <Link to={{ pathname: "/cook", state: { recipe: recipe } }}>
                <Button className="lets-go-btn" variant="primary">
                  Let's Go!
                </Button>
              </Link>
            </div>
          );
          return <Recipe key={index} recipe={recipe} buttons={button} />;
        })}
      </div>
    );
  }
}
export default History;
