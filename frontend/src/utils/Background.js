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
import bg_1 from "../background-images/breakfast.jpeg";
import bg_2 from "../background-images/japanese.jpeg";
import bg_3 from "../background-images/korean.jpeg";
import bg_4 from "../background-images/bulgarian.jpeg";
import bg_5 from "../background-images/smoothie-bowl.jpeg";

const backgrounds = [bg_1, bg_2, bg_3, bg_4, bg_5];

export function getBackground() {
  const index = Math.floor(Math.random() * backgrounds.length);
  return (
    <div className="background-div">
      <img className="bg-image" src={backgrounds[index]} alt="bg" />
    </div>
  );
}
