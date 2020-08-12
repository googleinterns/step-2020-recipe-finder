import React from "react";
import "./App.css";
import CookRecipe from "./components/CookRecipe";
import InputText from "./components/InputText";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import RecommendedRecipes from "./components/RecommendedRecipes";
import "bootstrap/dist/css/bootstrap.min.css";

function App() {
  return (
    <div className="App">
      <Router>
        <div>
          <Switch>
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
