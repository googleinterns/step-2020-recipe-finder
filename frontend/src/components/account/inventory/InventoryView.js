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
import React from "react";
import Button from "react-bootstrap/Button";
import { Link } from "react-router-dom";
import "./Inventory.css";
import ComponentWithHeader from "../../header/ComponentWithHeader";

//page to view inventory with button to click edit


class InventoryView extends ComponentWithHeader {
  constructor(properties) {
    super(properties);
    this.state = {
      inventory: [],
    };
  }
}
export default InventoryView;
