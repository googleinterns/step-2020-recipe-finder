import React, { Component } from "react";
import { Redirect } from "react-router-dom";

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
          <div class="spinner-border" role="status">
            <span class="sr-only">Loading...</span>
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
  return (
    <div>
      <h1>{recipe.name}</h1>
      <h3>Cooking time: {recipe.time}</h3>
      <h3>Level: {recipe.level}</h3>
      <h3>Per serving: {recipe.calories}</h3>
      <button onClick={props.onClick}>Let's cook</button>
    </div>
  );
}
export default RecommendedRecipes;
