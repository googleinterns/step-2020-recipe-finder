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

import React from "react";
import Button from "react-bootstrap/Button";
import EyeIcon from "../../icons/eye.svg";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import Popover from "react-bootstrap/Popover";

export function Recipe(props) {
  const recipe = props.recipe;
  const previewPopover = (
    <Popover>
      <Popover.Title as="h3">Recipe preview</Popover.Title>
      <Popover.Content>
        {recipe.instructions.map((step, i) => (
          <p key={i}>{step}</p>
        ))}
      </Popover.Content>
    </Popover>
  );

  return (
    <div className="dish">
      <div className="dish-header">
        <h1 className="dish-name">{recipe.name}</h1>
        <OverlayTrigger
          trigger="click"
          placement="left"
          overlay={previewPopover}
        >
          <div className="right-side-btn">
            <Button variant="link">
              <img src={EyeIcon} alt="recipe-preview" /> Preview
            </Button>
          </div>
        </OverlayTrigger>
      </div>
      <div className="dish-contents-container">
        <div className="dish-contents">
          <p>Cooking time: {recipe.time}</p>
          <p>Difficulty: {recipe.difficulty}</p>
          <p>Per serving: {recipe.calories}</p>
        </div>
        {props.buttons}
      </div>
    </div>
  );
}
