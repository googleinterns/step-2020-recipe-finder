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

class RecommendedRecipes extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: true,
      isRedirect: false,
      recipes: [],
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
      return <Redirect to="/cook" />;
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
    localStorage.setItem("recipe", JSON.stringify(recipe));
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
