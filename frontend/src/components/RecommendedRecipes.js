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

// MIT License

// Copyright (c) Facebook, Inc. and its affiliates.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

// The MIT License (MIT)

// Copyright (c) 2014-present Stephen J. Collings, Matthew Honnibal, Pieter Vanderwerff

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

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
      // hard-coded data
      recipes: [
        {
          name: "Dish 1",
          time: "18 min",
          difficulty: "Easy",
          calories: "383 kcal",
          ingredients: ["broccoli", "tomato"],
          instructions: ["step1: broccoli", "step2: tomato"],
        },
        {
          name: "Dish 2",
          time: "25 min",
          difficulty: "Hard",
          calories: "243 kcal",
          ingredients: ["egg", "tomato"],
          instructions: ["step1: egg", "step2: tomato"],
        },
        {
          name: "Dish 3",
          time: "20 min",
          difficulty: "Easy",
          calories: "342 kcal",
          ingredients: ["egg", "pasta"],
          instructions: ["step1: egg", "step2: pasta"],
        },
      ],
    };
  }

  componentDidMount() {
    // post request to get recommended recipes
    this.setState({ isLoading: false });
  }

  render() {
    if (this.state.isLoading) {
      return (
        <div>
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
        <OverlayTrigger trigger="click" placement="left" overlay={previewPopover}>
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
            Let's cook!
          </Button>
        </div>
      </div>
    </div>
  );
}
export default RecommendedRecipes;
