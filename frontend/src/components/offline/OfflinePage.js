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
import React, { Component } from "react";
import { getOneBackground } from "../../utils/Background";
import "./OfflinePage.css";
import Walkthrough from "../login/Walkthrough";

class OfflinePage extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      background: getOneBackground(),
      showModal: false,
    };
    this.handleClose = this.handleClose.bind(this);
    this.setShowModal = this.setShowModal.bind(this);
  }

  render() {
    return (
      <div id="offline-div">
        <Walkthrough
          handleClose={this.handleClose}
          showModal={this.state.showModal}
        />
        {this.state.background}
        <h1 className="white-text text-center">Recipe Finder</h1>
        <div className="offline-message">
          You're offline, come back when you're connected to the internet.
        </div>
        <div className="offline-btns-div">
          <Button
            variant="secondary"
            onClick={this.setShowModal}
            className="walkthrough-btn"
          >
            Show me how Recipe Finder works
          </Button>
        </div>
      </div>
    );
  }

  handleClose() {
    this.setState({ showModal: false });
  }

  setShowModal() {
    this.setState({ showModal: true });
  }
}
export default OfflinePage;
