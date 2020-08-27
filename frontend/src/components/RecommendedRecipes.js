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
import "./RecommendedRecipes.css";
import { Recipe } from "./Recipe";

class RecommendedRecipes extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      isLoading: true,
      isRedirect: false,
      recipes: [],
      chosenRecipe: {},
    };
  }

  // retrieves recommended recipes from the back end
  componentDidMount() {
    const request = new Request("/api/find-recipes", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
    });
    fetch(request)
      .then((response) => response.json())
      .then((json) => this.setState({ recipes: json, isLoading: false }))
      .catch((err) => console.log(err));
  }

  render() {
    if (this.state.isLoading) {
      return (
        <div className="spinner-div">
          <div className="spinner-border" role="status">
            <span className="sr-only">Loading...</span>
          </div>
          <h3>Scanning recipes...</h3>
        </div>
      );
    }

    if (this.state.isRedirect) {
      return (
        <Redirect
          to={{ pathname: "/cook", state: { recipe: this.state.chosenRecipe } }}
        />
      );
    }

    return (
      <div>
        {this.state.recipes.map((recipe, index) => {
          const button = (
            <div className="right-side-btn">
              <Button
                variant="primary"
                onClick={this.setRecipeAndRedirect(recipe)}
              >
                Let's Go!
              </Button>
            </div>
          );
          return <Recipe key={index} recipe={recipe} buttons={button} />;
        })}
      </div>
    );
  }

  setRecipeAndRedirect(recipe) {
    try {
      localStorage.setItem("recipe", JSON.stringify(recipe));
      localStorage.setItem(
        "tutorial-step",
        /*starting index for tutorial's carousel*/ 0
      );
    } catch (error) {
      this.setState({ chosenRecipe: recipe });
    }
    this.setState({ isRedirect: true });
  }
}
export default RecommendedRecipes;
