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
import speakerOn from "../../icons/speaker-on.svg";
import speakerOff from "../../icons/speaker-off.svg";
import { Link } from "react-router-dom";
import { readStep } from "./TextToSpeech";

class Tutorial extends Component {
  constructor(properties) {
    super(properties);
    this.state = {
      isLastStep: false,
      isSpeakerOff: false,
    };
    this.switchSpeaker = this.switchSpeaker.bind(this);
  }

  componentDidMount() {
    this.noteIfLastStep();
    readStep(this.props.recipe.instructions[this.getSelectedStep()]);
  }

  render() {
    return (
      <div>
        <div className="centered-div">
          <Button variant="primary" onClick={this.switchSpeaker}>
            <img src={this.getSpeakerIcon()} alt="switch speaker" />
            {this.getSpeakerMessage()}
          </Button>
        </div>

        <Carousel
          interval={null} // to disable auto play of the carousel
          onSelect={this.setSelectedStepAndMaybeRead}
          defaultActiveIndex={this.getSelectedStep}
          nextIcon={
            <img
              src={navigateNext}
              alt="next step"
              className="carousel-control"
            />
          }
          prevIcon={
            <img
              src={navigatePrevious}
              alt="previous step"
              className="carousel-control"
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

  setSelectedStepAndMaybeRead = (selectedIndex, e) => {
    localStorage.setItem("tutorial-step", selectedIndex);
    this.noteIfLastStep();
    if (this.state.isSpeakerOff) {
      return;
    }
    readStep(this.props.recipe.instructions[selectedIndex]);
  };

  getSelectedStep() {
    const step = JSON.parse(localStorage.getItem("tutorial-step"));
    return step ? step : 0;
  }

  noteIfLastStep() {
    const step = this.getSelectedStep();
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
            <Button>All done!</Button>
          </Link>
        </div>
      );
    }
  }

  switchSpeaker() {
    const previousStateIsSpeakerOff = this.state.isSpeakerOff;
    this.setState({ isSpeakerOff: !previousStateIsSpeakerOff });
  }

  finishCooking(recipe) {
    const request = new Request("/api/store-recipe", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(recipe)
    });

  }

  getSpeakerIcon() {
    return this.state.isSpeakerOff ? speakerOn : speakerOff;
  }

  getSpeakerMessage() {
    return this.state.isSpeakerOff ? "Always read steps" : "Don't read steps";
  }
}
export default Tutorial;
