import React from 'react';
import './App.css';
import CookRecipe from "./components/CookRecipe";
import InputText from "./components/InputText";
import {BrowserRouter as Router, Switch, Route, Link} from "react-router-dom";

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
              <Link to="/cook">Cook</Link>
            </li>
          </ul>
        </nav>

        <Switch>
          <Route exact path="/text" component={InputText}/>
          <Route exact path="/cook" component={CookRecipe}/>
        </Switch>
      </div>
      </Router>
    </div>
  );
}

export default App;
