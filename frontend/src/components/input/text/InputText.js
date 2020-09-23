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

import React from "react";
import InputTextItems from "./InputTextItems";
import "./InputText.css";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { Link } from "react-router-dom";
import { backButton } from "../../utils/Utilities";
import ComponentWithHeader from "../../header/ComponentWithHeader";

class InputText extends ComponentWithHeader {
  constructor(properties) {
    super(properties);

    this.state = {
      items: [],
      inventory: [],
    };

    this.addItem = this.addItem.bind(this);
    this.deleteItem = this.deleteItem.bind(this);
  }

  componentDidMount() {
    const { inventory } = this.props.location.state;
    this.setState({inventory: inventory});
  }
  addItem(event) {
    const value = this._inputElement.value;
    if (value === "") {
      event.preventDefault();
      return;
    }
    var newItem = {
      text: value.charAt(0).toUpperCase() + value.slice(1).toLowerCase(),
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

  renderContent() {
    const ingredients = this.state.items.map((item) => item.text);
    return (
      <div>
        {backButton()}
        <div className="ingredient-list">
          <h1>Input Ingredients</h1>
          <Form onSubmit={this.addItem}>
            <Form.Row>
              <div className="input">
                <Form.Control
                  ref={(a) => (this._inputElement = a)}
                  type="text"
                  placeholder="New Ingredient"
                />
              </div>

              <div>
                <Button type="submit">Add</Button>
              </div>

              <div>
                <Link
                  to={{
                    pathname: "/recommendations",
                    state: {
                      ingredients: ingredients,
                    },
                  }}
                >
                  <Button disabled={this.state.items.length === 0}>
                    Confirm
                  </Button>
                </Link>
              </div>
            </Form.Row>
          </Form>

          <InputTextItems entries={this.state.items} delete={this.deleteItem} />
        </div>
      </div>
    );
  }
}
export default InputText;
