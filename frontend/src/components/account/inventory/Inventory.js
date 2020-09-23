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
import React, {Component} from 'react';
import InventoryItems from "./InventoryItems";
import { loading } from "../../utils/Utilities";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { Link } from "react-router-dom";
import "./Inventory.css";
import ComponentWithHeader from "../../header/ComponentWithHeader";

class Inventory extends ComponentWithHeader {
  constructor(properties) {
    super(properties);
    this.state = {
      inventory: [],
    };

    this.addItem = this.addItem.bind(this);
    this.deleteItem = this.deleteItem.bind(this);
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
      prevState.inventory.forEach(function (item, index, object) {
        if (item.text === newItem.text) {
          isDuplicate = true;
          window.alert("Duplicate Ingredient");
        }
      });
      if (isDuplicate) {
        return {
          inventory: prevState.inventory,
        };
      } else {
        return {
          inventory: [newItem].concat(prevState.inventory),
        };
      }
    });
    this._inputElement.value = "";
    event.preventDefault();
  }

  deleteItem(key) {
    var filteredinventory = this.state.inventory.filter(function (item) {
      return item.key !== key;
    });

    this.setState({
      inventory: filteredinventory,
    });
  }
  componentDidMount() {
    this.getInventory();
  }
    
  renderContent() {
    const inventory = this.state.inventory.map((item) => item.text);
    return(
      <div>
        <div className="centered-container">
          <h1 className="inventory-page-title">Inventory</h1>
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
            </Form.Row>
          </Form>
          <p>{this.getMessageIfNoInventory()}</p>

          <InventoryItems entries={this.state.inventory} delete={this.deleteItem} />
          </div>
          <Button type="confirm" onClick={this.saveToInventory(inventory)} href="/home">Confirm</Button>
        </div>
    )
  }
  getInventory() {
    fetch("/api/inventory")
      .then((response) => response.json())
      .then((json) => this.setState({ inventory: json }))
      .catch((err) => console.log(err))
      .finally(() => this.setState({ loading: false }));
  }
  getMessageIfNoInventory() {
    if (this.state.inventory.length === 0) {
      return "Empty Pantry?";
    }
  }
  saveToInventory(ingredient) {
    console.log(ingredient);
    const request = new Request("/api/inventory", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(ingredient),
    });
    fetch(request).catch((err) => console.log(err));
  }
}

export default Inventory;
