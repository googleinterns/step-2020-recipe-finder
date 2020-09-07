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
import Tab from "react-bootstrap/Tab";
import Tabs from "react-bootstrap/Tabs";
import "./CookRecipe.css";
import Tutorial from "./Tutorial";
import { backButton } from "../utils/Utilities";

class CookRecipe extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      recipe: localStorage.getItem("recipe")
        ? JSON.parse(localStorage.getItem("recipe"))
        : this.props.location.state.recipe,
    };
  }

  render() {
    const recipe = this.state.recipe;
    const renderHTML = (rawHTML) => React.createElement("div", { dangerouslySetInnerHTML: { __html: rawHTML } });
    return (
      <div>
        {backButton()}
        <h1>{recipe.name}</h1>
        <Tabs defaultActiveKey="tutorial">
          <Tab eventKey="ingredients" title="Ingredients">
            <div className="tab-content">
              <ul>
                {recipe.ingredients.map((item, i) => (
                  <li key={i}>{renderHTML(item)}</li>
                ))}
              </ul>
            </div>
          </Tab>
          <Tab eventKey="full-recipe" title="Full recipe">
            <div className="tab-content">
              <ol>
                {recipe.instructions.map((step, i) => (
                  <li key={i}>{step}</li>
                ))}
              </ol>
            </div>
          </Tab>
          <Tab eventKey="tutorial" title="Tutorial">
            <div className="tab-content">
              <Tutorial recipe={this.state.recipe} />
            </div>
          </Tab>
        </Tabs>
      </div>
    );
  }

}
export default CookRecipe;
