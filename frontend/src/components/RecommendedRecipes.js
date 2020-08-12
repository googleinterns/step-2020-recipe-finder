import Button from "react-bootstrap/Button";
import EyeIcon from "../icons/eye.svg";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import Popover from "react-bootstrap/Popover";
import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import "./RecommendedRecipes.css";

class RecommendedRecipes extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: true,
      isRedirect: false,
      // hard-coded data
      recipes: [
        {
          name: "Dish 1",
          time: "18 min",
          level: "Easy",
          calories: "383 kcal",
          ingredients: ["broccoli", "tomato"],
          instructions: ["step1: broccoli", "step2: tomato"],
        },
        {
          name: "Dish 2",
          time: "25 min",
          level: "Hard",
          calories: "243 kcal",
          ingredients: ["egg", "tomato"],
          instructions: ["step1: egg", "step2: tomato"],
        },
        {
          name: "Dish 3",
          time: "20 min",
          level: "Easy",
          calories: "342 kcal",
          ingredients: ["egg", "pasta"],
          instructions: ["step1: egg", "step2: pasta"],
        },
      ],
    };
  }

  componentDidMount() {
    // post request to get recommended recipes
    this.setState({ isLoading: false });
  }

  render() {
    if (this.state.isLoading) {
      return (
        <div>
          <div className="spinner-border" role="status">
            <span className="sr-only">Loading...</span>
          </div>
          <h3>Scanning recipes...</h3>
        </div>
      );
    }

    if (this.state.isRedirect) {
      return <Redirect to="/cook" />;
    }

    return (
      <div>
        {this.state.recipes.map((recipe, i) => (
          <Recipe
            key={i}
            recipe={recipe}
            onClick={() => this.onClick(recipe)}
          />
        ))}
      </div>
    );
  }

  onClick(recipe) {
    localStorage.setItem("recipe", JSON.stringify(recipe));
    this.setState({ isRedirect: true });
  }
}

function Recipe(props) {
  const recipe = props.recipe;
  const popover = (
    <Popover id="popover-basic">
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
        <OverlayTrigger trigger="click" placement="left" overlay={popover}>
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
          <p>Level: {recipe.level}</p>
          <p>Per serving: {recipe.calories}</p>
        </div>

        <div className="right-side-btn">
          <Button variant="primary" onClick={props.onClick}>
            Let's cook!
          </Button>
        </div>
      </div>
    </div>
  );
}
export default RecommendedRecipes;
