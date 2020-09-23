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
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import home from "../../icons/home.svg";
import "./AccountHeader.css";
import account from "../../icons/account.svg";
import inventory from "../../icons/inventory.svg";
import favourite from "../../icons/favourite.svg";
import history from "../../icons/history.svg";
import tour from "../../icons/tour.svg";
import Walkthrough from "../login/Walkthrough";
import { Button } from "react-bootstrap";

class AccountHeader extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      showModal: false,
    };

    this.showModal = this.showModal.bind(this);
    this.handleClose = this.handleClose.bind(this);
  }

  render() {
    return (
      <div className="navbar-div">
        <Walkthrough
          handleClose={this.handleClose}
          showModal={this.state.showModal}
        />
        <Navbar id="account-header" fixed="top" expand="lg">
          <Navbar.Brand href="/home">
            <img src={home} alt="home" />
            Recipe Finder
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="mr-auto">
              <Nav.Link href="/account">
                <img src={account} alt="account" className="header-icon" />
                My Account
              </Nav.Link>
              <Nav.Link href="/favourites">
                <img src={favourite} alt="favourites" className="header-icon" />
                Favourites
              </Nav.Link>
              <Nav.Link href="/history">
                <img src={history} alt="history" className="header-icon" />
                History
              </Nav.Link>
              <Nav.Link href="/inventory">
                  <img src={inventory} alt="inventory" className="header-icon" />
                  Inventory
              </Nav.Link>
              <Button className="walkthrough-link" onClick={this.showModal}>
                <img src={tour} alt="walkthrough" className="header-icon" />
                How does Recipe Finder work?
              </Button>
            </Nav>
          </Navbar.Collapse>
        </Navbar>
      </div>
    );
  }

  handleClose() {
    this.setState({ showModal: false });
  }

  showModal() {
    this.setState({ showModal: true });
  }
}
export default AccountHeader;
