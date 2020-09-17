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
import React from "react";
import { Redirect } from "react-router-dom";
import "./RecommendedRecipes.css";
import fridge from "../../icons/fridge.svg";
import Popover from "react-bootstrap/Popover";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import { handleResponseError } from "../utils/APIErrorHandler";
import { errorRedirect } from "../utils/APIErrorHandler";
import { loading, backButton } from "../utils/Utilities";
import { Recipe } from "../recipe/Recipe";
import ComponentWithHeader from "../header/ComponentWithHeader";

class RecommendedRecipes extends ComponentWithHeader {
  constructor(properties) {
    super(properties);
    this.state = {
      loading: true,
      isRedirect: false,
      recipes: [],
      chosenRecipe: {},
      error: null,
      ingredients: [],
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
      .then((json) =>
        this.setState({
          recipes: json,
          loading: false,
          ingredients: ingredients,
        })
      )
      .catch((error) => this.setState({ error: error }));
  }

  renderContent() {
    if (this.state.error !== null) {
      return errorRedirect(this.state.error);
    }

    if (this.state.loading) {
      return loading("Scanning recipes...", /** withBackground*/ true);
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
                <OverlayTrigger
                  trigger="focus"
                  placement="auto"
                  overlay={this.ingredientsPopover(recipe)}
                >
                  <div>
                    <Button variant="link" className="ingredients-btn">
                      <img src={fridge} alt="ingredients" /> Ingredients
                    </Button>
                  </div>
                </OverlayTrigger>
                <Button
                  className="recommended-lets-go"
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

  ingredientsPopover(recipe) {
    const renderHTML = (rawHTML) =>
      React.createElement("div", {
        dangerouslySetInnerHTML: { __html: rawHTML },
      });
    return (
      <Popover>
        <Popover.Title as="h3">Ingredients</Popover.Title>
        <Popover.Content>
          {recipe.ingredients.map((item, i) => (
            <p key={i}>{renderHTML(item)}</p>
          ))}
        </Popover.Content>
      </Popover>
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
      sessionStorage.setItem("recipe", JSON.stringify(recipe));
      sessionStorage.setItem(
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
