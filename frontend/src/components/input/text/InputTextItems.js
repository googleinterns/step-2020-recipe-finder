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
import Button from "react-bootstrap/Button";

class InputTextItems extends Component {
  constructor(properties) {
    super(properties);

    this.createItems = this.createItems.bind(this);
  }

  delete(key) {
    this.props.delete(key);
  }

  createItems(item) {
    return (
      <li key={item.key}>
        <div className="ingredient-text">{item.text}</div>
        <div className="remove-ingredient-div">
          <Button variant="link" onClick={() => this.delete(item.key)}>X</Button>
        </div>
      </li>
    );
  }

  render() {
    var listItems = this.props.entries.map(this.createItems);
    return <ul className="the-list">{listItems}</ul>;
  }
}

export default InputTextItems;
