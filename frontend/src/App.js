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
import CookRecipe from "./components/individual-recipe/CookRecipe";
import FinishedRecipe from "./components/individual-recipe/FinishedRecipe";
import InputText from "./components/input/text/InputText";
import ShoppingList from "./components/account/shopping/ShoppingList";
import Inventory from "./components/account/inventory/Inventory";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import RecommendedRecipes from "./components/recommended-recipes/RecommendedRecipes";
import Home from "./components/home/Home";
import Login from "./components/login/Login";
import SignUp from "./components/account/sign-up/SignUp";
import Favourites from "./components/account/favourites/Favourites";
import Account from "./components/account/my-account/Account";
import AccountHistory from "./components/account/history/History";
import ReactErrorHandler from "./components/utils/ReactErrorHandler";
import ErrorPage from "./components/utils/APIErrorHandler";
import { PrivateRoute } from "./components/utils/PrivateRoute";

function App() {
  return (
    <div className="App">
      <Router>
        <ReactErrorHandler>
          <Switch>
            <Route exact path="/" component={Login} />
            <Route exact path="/error" component={ErrorPage} />
            <PrivateRoute exact path="/sign-up" component={SignUp} />
            <PrivateRoute exact path="/home" component={Home} />
            <PrivateRoute exact path="/text" component={InputText} />
            <PrivateRoute exact path="/account" component={Account} />
            <PrivateRoute exact path="/cook" component={CookRecipe} />
            <PrivateRoute exact path="/shop" component={ShoppingList} />
            <PrivateRoute exact path="/history" component={AccountHistory} />
            <PrivateRoute exact path="/pantry" component={Inventory} />
            <PrivateRoute exact path="/favourites" component={Favourites} />
            <PrivateRoute exact path="/finished" component={FinishedRecipe} />
            <PrivateRoute
              exact
              path="/recommendations"
              component={RecommendedRecipes}
            />
          </Switch>
        </ReactErrorHandler>
      </Router>
    </div>
  );
}

export default App;
