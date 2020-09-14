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
import Carousel from "react-bootstrap/Carousel";
import React, { Component } from "react";
import "./CookRecipe.css";
import navigateNext from "../../icons/navigate_next.svg";
import navigatePrevious from "../../icons/navigate_previous.svg";
import { Link } from "react-router-dom";

class Tutorial extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      isLastStep: false,
    };

    this.getSelectedStep = this.getSelectedStep.bind(this);
  }

  componentDidMount() {
    this.noteIfLastStep();
    if (
      !this.state.showModal &&
      this.props.isSpeakerOn &&
      this.props.isTutorialActive
    ) {
      this.readStep(this.getSelectedStep());
    }
  }

  render() {
    return (
      <div>
        <audio
          controls
          controlsList="nodownload"
          id="audio"
          style={{ display: this.props.isSpeakerOn ? "block" : "none" }}
        >
          <source src="" id="source" />
          Your browser does not support the audio element.
        </audio>
        <Carousel
          interval={null} // to disable auto play of the carousel
          onSelect={this.setSelectedStepAndMaybeRead}
          defaultActiveIndex={this.getSelectedStep}
          nextIcon={
            <img
              src={navigateNext}
              alt="next step"
              className={this.getIconClassName(/** isNext*/ true)}
            />
          }
          prevIcon={
            <img
              src={navigatePrevious}
              alt="previous step"
              className={this.getIconClassName(/** isNext*/ false)}
            />
          }
        >
          {this.props.recipe.instructions.map((step, i) => (
            <Carousel.Item key={i} className="carousel-step">
              {step}
            </Carousel.Item>
          ))}
        </Carousel>
        {this.displayFinishedIfLastStep()}
      </div>
    );
  }

  getIconClassName(isNext) {
    const carousel_control = "carousel-control";
    const carousel_control_hidden = carousel_control + " d-none";
    if (isNext) {
      if (
        this.getSelectedStep() ===
        this.props.recipe.instructions.length - 1
      ) {
        return carousel_control_hidden;
      }
    } else {
      if (this.getSelectedStep() === 0) {
        return carousel_control_hidden;
      }
    }
    return carousel_control;
  }

  getSelectedStep() {
    return this.props.getSelectedStep();
  }

  readStep(index) {
    return this.props.readStep(index);
  }

  setSelectedStepAndMaybeRead = (selectedIndex, e) => {
    try {
      sessionStorage.setItem("tutorial-step", selectedIndex);
    } catch (error) {
      console.log(error);
    }
    this.noteIfLastStep();
    if (this.props.isSpeakerOn) {
      this.readStep(selectedIndex);
    }
  };

  noteIfLastStep() {
    const step = this.props.getSelectedStep();
    const isLastStep = this.props.recipe.instructions.length === step + 1;
    this.setState({ isLastStep: isLastStep });
  }

  displayFinishedIfLastStep() {
    if (this.state.isLastStep) {
      return (
        <div className="centered-div">
          <Link
            to={{
              pathname: "/finished",
              state: {
                recipeName: this.props.recipe.name,
                recipeId: this.props.recipe.recipeId,
              },
            }}
            onClick={() => this.finishCooking(this.props.recipe)}
          >
            <Button className="all-done-btn">All done!</Button>
          </Link>
        </div>
      );
    }
  }

  finishCooking(recipe) {
    const request = new Request("/api/store-recipe", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(recipe),
    });
    fetch(request).catch((err) => console.log(err));
    sessionStorage.removeItem("tutorial-step");
  }
}
export default Tutorial;
