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
import { Link } from "react-router-dom";

class FinishedRecipe extends Component {
  render() {
    return (
      <div>
        <h1>Well Done!</h1>
        <h4>Did you like cooking {this.props.location.state.recipeName}?</h4>
        <Link to="/home">
          <Button onClick={this.saveToFavourites}>Save to my favourites</Button>
        </Link>
      </div>
    );
  }

  saveToFavourites() {
    // to do: backend get request to save this recipe to favourites
  }
}
export default FinishedRecipe;
