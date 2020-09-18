package com.google.sps.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.sps.utils.TestUtils;
import org.junit.Test;

public class RecipeTest {
  private static final Recipe RECIPE = TestUtils.getRecipe();

  @Test
  public void testExistingContainsDietaryRequirement() {
    String diet = RECIPE.getDietaryRequirements().get(0);
    assertTrue(RECIPE.containsDietaryRequirement(diet));
  }

  @Test
  public void testNonExistingContainsDietaryRequirement() {
    String diet = "halal";
    assertFalse(RECIPE.containsDietaryRequirement(diet));
  }

  @Test
  public void testExistingContainsIngredient() {
    String ingredient = RECIPE.getIngredients().get(0);
    assertTrue(RECIPE.containsIngredient(ingredient));

    String partOfIngredient = ingredient.substring(ingredient.length() / 2);
    assertTrue(RECIPE.containsIngredient(partOfIngredient));
  }

  @Test
  public void testNonExistingContainsIngredient() {
    String ingredient = "potato";
    assertFalse(RECIPE.containsIngredient(ingredient));
  }
}
