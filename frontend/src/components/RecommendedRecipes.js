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
import EyeIcon from "../icons/eye.svg";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import Popover from "react-bootstrap/Popover";
import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import "./RecommendedRecipes.css";
import { handleResponseError } from "./utils/GeneralErrorHandler";
import { errorRedirect } from "./utils/GeneralErrorHandler";
import { loading } from "./utils/Utilities";

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
    const request = new Request("/api/find-recipes", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
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
        {this.state.recipes.map((recipe, i) => (
          <Recipe
            key={i}
            recipe={recipe}
            onClickLetsCook={() => this.setRecipeAndRedirect(recipe)}
          />
        ))}
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

function Recipe(props) {
  const recipe = props.recipe;
  const previewPopover = (
    <Popover>
      <Popover.Title as="h3">Recipe preview</Popover.Title>
      <Popover.Content>
        {recipe.instructions.map((step, i) => (
          <p key={i}>{step}</p>
        ))}
      </Popover.Content>
    </Popover>
  );

  return (
    <div className="dish">
      <div className="dish-header">
        <h1 className="dish-name">{recipe.name}</h1>
        <OverlayTrigger
          trigger="click"
          placement="left"
          overlay={previewPopover}
        >
          <div className="right-side-btn">
            <Button variant="link">
              <img src={EyeIcon} alt="recipe-preview" /> Preview
            </Button>
          </div>
        </OverlayTrigger>
      </div>
      <div className="dish-contents-container">
        <div className="dish-contents">
          <p>Cooking time: {recipe.time}</p>
          <p>Difficulty: {recipe.difficulty}</p>
          <p>Per serving: {recipe.calories}</p>
        </div>

        <div className="right-side-btn">
          <Button variant="primary" onClick={props.onClickLetsCook}>
            Let's Go!
          </Button>
        </div>
      </div>
    </div>
  );
}
export default RecommendedRecipes;
