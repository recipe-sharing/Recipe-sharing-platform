package lt.techin.recipesharingplatform.services;

import lt.techin.recipesharingplatform.models.Category;
import lt.techin.recipesharingplatform.models.Recipe;
import lt.techin.recipesharingplatform.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe createRecipe(String recipeName, String description, Category category) {
        Recipe recipe = new Recipe();
        recipe.setRecipeName(recipeName);
        recipe.setDescription(description);
        recipe.setCategory(category);

        return recipeRepository.save(recipe);
    }
}