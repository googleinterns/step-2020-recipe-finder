/* Copyright 2020 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

import React, { Component } from "react";
import InputTextItems from "./InputTextItems";
import "./InputText.css";
import Button from "react-bootstrap/Button";
import { Link } from "react-router-dom";
import Button from "react-bootstrap/Button";

class InputText extends Component {
  constructor(properties) {
    super(properties);

    this.state = {
      items: [],
    };

    this.addItem = this.addItem.bind(this);
    this.deleteItem = this.deleteItem.bind(this);
  }

  addItem(event) {
    if (this._inputElement.value === "") {
      return;
    }
    var newItem = {
      text:
        this._inputElement.value.charAt(0).toUpperCase() +
        this._inputElement.value.slice(1).toLowerCase(),
      key: Date.now(),
    };
    var isDuplicate = false;
    this.setState((prevState) => {
      prevState.items.forEach(function (item, index, object) {
        if (item.text === newItem.text) {
          isDuplicate = true;
          window.alert("Duplicate Ingredient");
        }
      });
      if (isDuplicate) {
        return {
          items: prevState.items,
        };
      } else {
        return {
          items: [newItem].concat(prevState.items),
        };

      }
    });
    this._inputElement.value = "";
    event.preventDefault();
  }

  deleteItem(key) {
    var filteredItems = this.state.items.filter(function (item) {
      return item.key !== key;
    });

    this.setState({
      items: filteredItems,
    });
  }

  render() {
    const ingredients = this.state.items.map(item => item.text);
    return (
      <div className="ingredientList">
        <div className="header">
          <h1>Input Ingredients</h1>
          <form onSubmit={this.addItem}>
            <input
              ref={(a) => (this._inputElement = a)}
              placeholder="New Ingredient"
            ></input>
            <button type="submit">add</button>
          </form>
        </div>
        <Link
          to={{
            pathname: "/recommendations",
            state: {
              ingredients: ingredients
            },
          }}
        >
          <Button disabled={this.state.items.length === 0} variant="primary">Confirm</Button>
        </Link>
        <InputTextItems entries={this.state.items} delete={this.deleteItem} />
      </div>
    );
  }
}
export default InputText;
