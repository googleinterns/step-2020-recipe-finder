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

class Favourites extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      recipes: [{name: "Chicken Satay",
      time: "18 min",
      level: "Easy",
      calories: "383 kcal",
      ingredients: ["broccoli", "tomato"],
      instructions: ["step1: broccoli", "step2: tomato"], }]
    };
  }

  componentDidMount() {
    fetch("/api/favourites")
      .then((response) => response.json())
      .then((json) => this.setState({ recipes: json }))
      .catch((err) => console.log(err));
  }

  render() {
    return (
      <div>
        <AccountHeader /> 
        <h1>Favourites</h1>
        {this.state.recipes.map((recipe, index) => {
          const button = (
            <div className="right-side-btn">
              <Link to={{ pathname: "/cook", state: { recipe: recipe } }}>
                <Button className="lets-go-btn" variant="primary">
                  Let's Go!
                </Button>
              </Link>
              <Button
                className="remove-btn"
                variant="danger"
                onClick={this.removeFavourite}
              >
                Remove
              </Button>
            </div>
          );
          return <Recipe key={index} recipe={recipe} buttons={button} />;
        })}
      </div>
    );
  }

  removeFavourite = (recipe) => {
    const recipeId = recipe.recipeId;
    const request = new Request("/api/remove-favourite", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(recipeId),
    });
    fetch(request).catch((err) => console.log(err));
    window.location.reload();
  }
}
export default Favourites;
