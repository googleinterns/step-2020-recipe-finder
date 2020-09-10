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
import AccountHeader from "../account-header/AccountHeader";
import Button from "react-bootstrap/Button";
import { Link } from "react-router-dom";
import { Recipe } from "../../recipe/Recipe";
import "./Favourites.css";
import { loading } from "../../utils/Utilities";

class Favourites extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      recipes: [],
      isLoading: true
    };
  }

  componentDidMount() {
    this.getFavourites();
  }

  render() {
    if (this.state.isLoading) {
      return loading("Getting your favourite recipes ...");
    }

    return (
      <div>
        <AccountHeader />
        <div className="centered-container">
          <h1 className="account-page-title">Favourites</h1>
          <p>{this.getMessageIfNoFavourites()}</p>
          {this.state.recipes.map((recipe, index) => {
            const button = (
              <div className="right-side-btns">
                <Link to={{ pathname: "/cook", state: { recipe: recipe } }}>
                  <Button className="lets-go-btn" variant="primary">
                    Let's Go!
                  </Button>
                </Link>
                <Button
                  className="remove-btn"
                  variant="danger"
                  onClick={() => this.removeFavourite(recipe.recipeId)}
                >
                  Remove
                </Button>
              </div>
            );
            return <Recipe key={index} recipe={recipe} buttons={button} />;
          })}
        </div>
      </div>
    );
  }

  getMessageIfNoFavourites() {
    if (this.state.recipes.length === 0) {
      return "You didn't like any recipe yet";
    }
  }

  getFavourites() {
    fetch("/api/favourites")
      .then((response) => response.json())
      .then((json) => this.setState({ recipes: json }))
      .catch((err) => console.log(err))
      .finally(() => this.setState({isLoading: false}));
  }

  removeFavourite = (recipeId) => {
    const request = new Request("/api/remove-favourite", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(recipeId),
    });
    fetch(request)
      .then((response) => this.getFavourites())
      .catch((err) => console.log(err));
  };
}
export default Favourites;
