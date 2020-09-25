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
import { Link } from "react-router-dom";
import "./FinishedRecipe.css";
import ComponentWithHeader from "../header/ComponentWithHeader";

class FinishedRecipe extends ComponentWithHeader {
  constructor(properties) {
    super(properties);
    this.state = {
      error: null
    }
  }
  renderContent() {
    return (
      <div>
        <h1>Well Done!</h1>
        <h4>Did you like cooking {this.props.location.state.recipeName}?</h4>
        <Link
          to="/home"
          onClick={() =>
            this.saveToFavourites(this.props.location.state.recipeId)
          }
        >
          <Button className="finished-btns">Yes! Save to my favourites</Button>
        </Link>
        <Link to="/home">
          <Button variant="secondary" className="finished-btns">
            It was alright
          </Button>
        </Link>
      </div>
    );
  }

  saveToFavourites(recipeId) {
    const request = new Request("/api/favourites", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(recipeId),
    });
    fetch(request).catch((error) => this.setState({error: error}));
  }
}
export default FinishedRecipe;
