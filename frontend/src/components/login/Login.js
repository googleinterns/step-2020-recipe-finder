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
import { Redirect } from "react-router-dom";
import { handleResponseError } from "../utils/APIErrorHandler";
import { errorRedirect } from "../utils/APIErrorHandler";
import { loading } from "../utils/Utilities";
import "./Login.css";
import { getBackground } from "../../utils/Background";
import Modal from "react-bootstrap/Modal";
import Carousel from "react-bootstrap/Carousel";
import navigateNext from "../../icons/navigate_next.svg";
import navigatePrevious from "../../icons/navigate_previous.svg";
import signup from "../../walkthrough/sign-up.png";
import inputtext from "../../walkthrough/input-text.png";
import scanning from "../../walkthrough/scanning.png";
import recommended from "../../walkthrough/recommended.png";
import tutorial from "../../walkthrough/tutorial.png";
import favourites from "../../walkthrough/favourites.png";
import history from "../../walkthrough/history.png";
import home from "../../walkthrough/home.png";
import done from "../../walkthrough/done.png";

class Login extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      isLoggedIn: false,
      logUrl: "",
      error: null,
      isLoading: true,
      isFirstTime: false,
      showModal: false,
      background: getBackground()
    };

    this.handleClose = this.handleClose.bind(this);
    this.setShowModal = this.setShowModal.bind(this);
  }

  componentDidMount() {
    fetch("/api/login-status")
      .then(handleResponseError)
      .then((response) => response.json())
      .then((json) => {
        this.setState({
          isLoggedIn: json.isLoggedIn,
          logUrl: json.logUrl,
          isLoading: false,
          isFirstTime: json.isFirstTime,
          showModal: json.isFirstTime
        });
      })
      .catch((error) => this.setState({ error: error }));
  }

  render() {
    if (this.state.error !== null) {
      return errorRedirect(this.state.error);
    }

    if (this.state.isLoading) {
      return loading("Welcome! Checking if you're logged in ...", true);
    }

    if (this.state.isFirstTime) {
      this.trySavingSignOutToSessionStorage();
      if (this.state.showModal) {
      return this.getModal();
      } else {
      return <Redirect to="/sign-up" />;

      }
    }

    if (this.state.isLoggedIn) {
      this.trySavingSignOutToSessionStorage();
      return <Redirect to="/home" />;
    }

    return (
      <div>
        {this.getModal()}
        {this.state.background}
        <h1 className="white-text">Recipe Finder</h1>
        <div className="login-div">
          <Button variant="secondary" onClick={this.setShowModal}
              className="walkthrough-btn">Show me how Recipe Finder works</Button>
          <a href={this.state.logUrl}>
            <Button className="login-btn">Login</Button>
          </a>
        </div>
      </div>
    );
  }

   getModal() {
    return (
      <Modal show={this.state.showModal} className="walkthrough-carousel" backdrop="static" keyboard={false}>
        <Modal.Header>
          <Modal.Title className="w-100">Here's how Recipe Finder works:</Modal.Title>
        </Modal.Header>
        <Modal.Body>
               <Carousel
          interval={2000}
          onSelect={this.setSelectedStepAndMaybeRead}
          defaultActiveIndex={this.getSelectedStep}
          nextIcon={
            <img
              src={navigateNext}
              alt="next step"
            />
          }
          prevIcon={
            <img
              src={navigatePrevious}
              alt="previous step"
            />
          }
        >
          {this.getWalkthrough().map((step, i) => (
            <Carousel.Item key={i} className="walkthrough-step">
              <img src={step.image} alt="" className="walkthrough-image"/>
              <p className="walkthrough-text">
              {step.text}
              </p>
            </Carousel.Item>
          ))}
        </Carousel>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={this.handleClose}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  getWalkthrough() {
    return [
        {text: "1. Login with your Google account and enter your name with dietary requirements", image: signup},
        {text: "2. Enter your ingredients", image: inputtext},    
        {text: "3. Wait for us to look for suitable recipes for you", image: scanning},    
        {text: "4. Select a recipe you want to cook", image: recommended},    
        {text: "5. Cook with recipe steps read out to you", image: tutorial},    
        {text: "6. Enjoy eating your masterpiece and don't forget to save it to favourites if you liked it!", image: done},    
        {text: "You can view recipes that you've cooked in the past ..", image: favourites},    
        {text: ".. and view your favourite recipes in your account", image: history},    
        {text: "Welcome to Recipe Finder!", image: home},    
    ];
  }

  handleClose() {
    this.setState({showModal: false});    
  }

  setShowModal() {
    this.setState({showModal: true});
  }

  trySavingSignOutToSessionStorage() {
    try {
      sessionStorage.setItem("signOutUrl", this.state.logUrl);
    } catch (error) {
      console.log(error);
    }
  }
}
export default Login;
