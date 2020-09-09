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
import Modal from "react-bootstrap/Modal";
import Tab from "react-bootstrap/Tab";
import Tabs from "react-bootstrap/Tabs";
import "./CookRecipe.css";
import Tutorial from "./Tutorial";
import { backButton } from "../utils/Utilities";
import Button from "react-bootstrap/Button";

class CookRecipe extends Component {
  constructor(properties) {
    super(properties);
    const recipe = JSON.parse(localStorage.getItem("recipe"));
    const activeKey = sessionStorage.getItem("key");
    const isSpeakerOn = JSON.parse(sessionStorage.getItem("isSpeakerOn"));

    this.state = {
      recipe: recipe !== null ? recipe : this.props.location.state.recipe,
      key: activeKey !== null ? activeKey : "ingredients",
      audioSteps: new Array(recipe.instructions.length),
      isSpeakerOn: isSpeakerOn === null ? true : isSpeakerOn,
      showModal: isSpeakerOn === null ? true : false,
    };

    this.readStep = this.readStep.bind(this);
    this.switchSpeaker = this.switchSpeaker.bind(this);
  }

  render() {
    const recipe = this.state.recipe;
    const renderHTML = (rawHTML) =>
      React.createElement("div", {
        dangerouslySetInnerHTML: { __html: rawHTML },
      });
    return (
      <div>
        {this.getModal()}
        {backButton()}
        <h1>{recipe.name}</h1>
        <Tabs activeKey={this.state.key} onSelect={(key) => this.setKey(key)}>
          <Tab eventKey="ingredients" title="Ingredients">
            <div className="tab-content">
              <ul>
                {recipe.ingredients.map((item, i) => (
                  <li key={i}>{renderHTML(item)}</li>
                ))}
              </ul>
            </div>
          </Tab>
          <Tab eventKey="full-recipe" title="Full recipe">
            <div className="tab-content">
              <ol>
                {recipe.instructions.map((step, i) => (
                  <li key={i}>{step}</li>
                ))}
              </ol>
            </div>
          </Tab>
          <Tab eventKey="tutorial" title="Tutorial">
            <div className="tab-content">
              <Tutorial
                recipe={this.state.recipe}
                isTutorialActive={this.state.key === "tutorial"}
                readStep={this.readStep}
                getSelectedStep={this.getSelectedStep}
                isSpeakerOn={this.state.isSpeakerOn}
                switchSpeaker={this.switchSpeaker}
                pauseAudio={this.pauseAudio}
              />
            </div>
          </Tab>
        </Tabs>
      </div>
    );
  }

  getModal() {
    return (
      <Modal show={this.state.showModal} backdrop="static" keyboard={false}>
        <Modal.Header>
          <Modal.Title>Do you want the recipe to be read out loud?</Modal.Title>
        </Modal.Header>
        <Modal.Footer>
          <Button variant="primary" onClick={() => this.handleClose(true)}>
            Yes
          </Button>
          <Button variant="secondary" onClick={() => this.handleClose(false)}>
            No
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  handleClose(isSpeakerOn) {
    this.setState({ isSpeakerOn: isSpeakerOn, showModal: false });
    try {
      sessionStorage.setItem("isSpeakerOn", isSpeakerOn);
    } catch (error) {
      console.log(error);
    }
    if (isSpeakerOn && this.props.isTutorialActive) {
      this.readStep(this.getSelectedStep());
    }
  }

  setKey(key) {
    this.setState({ key: key });
    try {
      sessionStorage.setItem("key", key);
    } catch (error) {
      console.log(error);
    }
    if (key === "tutorial" && this.state.isSpeakerOn) {
      this.readStep(this.getSelectedStep());
    } else {
      this.pauseAudio();
    }
  }

  switchSpeaker() {
    const currentStateIsSpeakerOn = !this.state.isSpeakerOn;
    this.setState({ isSpeakerOn: currentStateIsSpeakerOn });
  }

  getSelectedStep() {
    const step = JSON.parse(sessionStorage.getItem("tutorial-step"));
    return step ? step : 0;
  }

  readStep(index) {
    if (this.state.audioSteps[index] !== undefined) {
      this.playAudio(this.state.audioSteps[index]);
      return;
    }

    const step = this.state.recipe.instructions[index];
    const request = new Request("/api/text-to-speech", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(step),
    });

    fetch(request)
      .then((response) => response.json())
      .then((json) => {
        const blob = new Blob([new Uint8Array(json)], { type: "audio/wav" });
        const url = URL.createObjectURL(blob);
        let audioSteps = this.state.audioSteps;
        audioSteps[index] = url;
        this.setState({ audioSteps: audioSteps });
        this.playAudio(url);
      })
      .catch((error) => console.log(error));
  }

  playAudio(url) {
    // audio and source are in Tutorial.js
    const audio = document.getElementById("audio");
    const source = document.getElementById("source");

    source.src = url;
    audio.load();
    var promise = audio.play();
    if (promise !== undefined) {
      promise
        .then((_) => {
          // autoplay started
        })
        .catch((error) => {
          console.log(error);
          // autoplay was prevented
        });
    }
  }

  pauseAudio() {
    const audio = document.getElementById("audio");
    var promise = audio.pause();
    if (promise !== undefined) {
      promise
        .then((_) => {
          // audio paused
        })
        .catch((error) => {
          console.log(error);
          // pause was prevented
        });
    }
  }
}
export default CookRecipe;
