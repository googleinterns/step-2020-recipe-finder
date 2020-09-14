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

import Modal from "react-bootstrap/Modal";
import Carousel from "react-bootstrap/Carousel";
import navigateNext from "../../icons/navigate_next.svg";
import navigatePrevious from "../../icons/navigate_previous.svg";
import signup from "../../walkthrough-images/sign-up.png";
import inputtext from "../../walkthrough-images/input-text.png";
import recommended from "../../walkthrough-images/recommended.png";
import tutorial from "../../walkthrough-images/tutorial.png";
import favourites from "../../walkthrough-images/favourites.png";
import history from "../../walkthrough-images/history.png";
import home from "../../walkthrough-images/home.png";
import done from "../../walkthrough-images/done.png";
import Button from "react-bootstrap/Button";
import React, { Component } from "react";
import "./Walkthrough.css";

class Walkthrough extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      activeIndex: 0,
    };
    this.handleSelect = this.handleSelect.bind(this);
  }

  render() {
    return (
      <Modal
        show={this.props.showModal}
        className="walkthrough-carousel"
        backdrop="static"
        keyboard={false}
      >
        <Modal.Header>
          <Modal.Title className="w-100">
            Here's how Recipe Finder works:
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Carousel
            indicators={false}
            activeIndex={this.state.activeIndex}
            onSelect={this.handleSelect}
            interval={2000}
            nextIcon={
              <img
                src={navigateNext}
                alt="next step"
                className={this.getNextIconClassName()}
              />
            }
            prevIcon={
              <img
                src={navigatePrevious}
                alt="previous step"
                className={this.getPreviousIconClassName()}
              />
            }
          >
            {this.getWalkthrough().map((step, i) => (
              <Carousel.Item key={i} className="walkthrough-step">
                <img src={step.image} alt="" className="walkthrough-image" />
                <p className="walkthrough-text">{step.text}</p>
              </Carousel.Item>
            ))}
          </Carousel>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={this.props.handleClose}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  handleSelect(selectedIndex) {
    this.setState({ activeIndex: selectedIndex });
  }

  getNextIconClassName() {
    if (this.state.activeIndex + 1 === this.getWalkthrough().length) {
      return "d-none";
    }
  }

  getPreviousIconClassName() {
    if (this.state.activeIndex === 0) {
      return "d-none";
    }
  }

  getWalkthrough() {
    return [
      {
        text:
          "1. Login with your Google account and enter your name with dietary requirements",
        image: signup,
      },
      { text: "2. Enter your ingredients", image: inputtext },
      { text: "3. Select a recipe you want to cook", image: recommended },
      { text: "4. Cook with recipe steps read out to you", image: tutorial },
      {
        text:
          "5. Enjoy eating your masterpiece and don't forget to save it to favourites if you liked it!",
        image: done,
      },
      {
        text: "You can view recipes that you've cooked in the past ..",
        image: favourites,
      },
      {
        text: ".. and view your favourite recipes in your account",
        image: history,
      },
      { text: "Welcome to Recipe Finder!", image: home },
    ];
  }
}
export default Walkthrough;
