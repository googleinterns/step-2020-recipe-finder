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

import React from "react";
import "./App.css";
import CookRecipe from "./components/CookRecipe";
import InputText from "./components/InputText";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import RecommendedRecipes from "./components/RecommendedRecipes";
import "bootstrap/dist/css/bootstrap.min.css";
import Home from "./components/Home";

function App() {
  return (
    <div className="App">
      <Router>
        <div>
          <Switch>
            <Route exact path="/" component={Home} />
            <Route exact path="/text" component={InputText} />
            <Route exact path="/cook" component={CookRecipe} />
            <Route
              exact
              path="/recommendations"
              component={RecommendedRecipes}
            />
          </Switch>
        </div>
      </Router>
    </div>
  );
}

export default App;
