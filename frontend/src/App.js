import React from "react";
import "./App.css";
import CookRecipe from "./components/CookRecipe";
import InputText from "./components/InputText";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import RecommendedRecipes from "./components/RecommendedRecipes";

function App() {
  return (
    <div className="App">
      <Router>
        <div>
          <nav>
            <ul>
              <li>
                <Link to="/text">Input Text</Link>
              </li>
              <li>
                <Link to="/recommendations">Recommendations</Link>
              </li>
            </ul>
          </nav>

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
