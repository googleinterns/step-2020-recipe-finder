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
import { handleResponseError } from "../utils/APIErrorHandler";
import { errorRedirect } from "../utils/APIErrorHandler";
import { loading, backButton } from "../utils/Utilities";
import { Recipe } from "../recipe/Recipe";

class RecommendedRecipes extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      isLoading: true,
      isRedirect: false,
      recipes: [],
      chosenRecipe: {},
      error: null,
    };
  }

  // retrieves recommended recipes from the back end
  componentDidMount() {
    const { ingredients } = this.props.location.state;
    const request = new Request("/api/find-recipes", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(ingredients),
    });
    fetch(request)
      .then(handleResponseError)
      .then((response) => response.json())
      .then((json) => this.setState({ recipes: json, isLoading: false }))
      .catch((error) => this.setState({ error: error }));
  }

  render() {
    if (this.state.error !== null) {
      return errorRedirect(this.state.error);
    }

    if (this.state.isLoading) {
      return loading("Scanning recipes ...");
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
        {backButton()}
        {this.getMessageIfNoRecipes()}
        <div className="centered-container">
          {this.state.recipes.map((recipe, index) => {
            const button = (
              <div className="right-side-btn">
                <Button
                  variant="primary"
                  onClick={() => this.setRecipeAndRedirect(recipe)}
                >
                  Let's Go!
                </Button>
              </div>
            );
            return <Recipe key={index} recipe={recipe} buttons={button} />;
          })}
        </div>
      </div>
    );
  }

  getMessageIfNoRecipes() {
    if (this.state.recipes.length === 0) {
      return (
        <h3 id="no-recipes-text">
          We couldn't find any recipes matching your ingredients, please try
          again with other ingredients
        </h3>
      );
    }
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
