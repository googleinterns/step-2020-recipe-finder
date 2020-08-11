import React, { Component } from "react";

class CookRecipe extends Component {
  constructor(props) {
    super(props);
    this.state = { recipe: JSON.parse(localStorage.getItem("recipe")) };
  }

  render() {
    const recipe = this.state.recipe;
    return (
      <div>
        <h1>{recipe.name}</h1>
        <h3>Ingredients</h3>
        <ul>
          {recipe.ingredients.map((item, i) => (
            <li key={i}>{item}</li>
          ))}
        </ul>
        <h3>Instructions</h3>
        <ol>
        {recipe.instructions.map((step, i) =>
        <li key={i}>{step}</li>
        )}
        </ol>
      </div>
    );
  }
}
export default CookRecipe;
