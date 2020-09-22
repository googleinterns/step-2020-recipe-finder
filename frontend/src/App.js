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
  // to enable code splitting of JS bundle
  // OfflinePage can be accessed by the serviceWorker whenever the network is offline
  const OfflinePage = React.lazy(() => import('./components/offline/OfflinePage'));

  return (
    <div className="App">
      <Router>
        <ReactErrorHandler>
          <Switch>
            <Route exact path="/" component={Login} />
            <Route exact path="/error" component={ErrorPage} />
            <Route exact path="/offline" component={OfflinePage} />
            <Route exact path="/sign-up" component={SignUp} />
            <Route exact path="/home" component={Home} />
            <Route exact path="/text" component={InputText} />
            <Route exact path="/account" component={Account} />
            <Route exact path="/cook" component={CookRecipe} />
            <Route exact path="/shop" component={ShoppingList} />
            <Route exact path="/history" component={AccountHistory} />
            <Route exact path="/inventory" component={Inventory} />
            <Route exact path="/favourites" component={Favourites} />
            <Route exact path="/finished" component={FinishedRecipe} />
            <Route
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
