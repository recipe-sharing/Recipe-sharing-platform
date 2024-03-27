package lt.techin.recipesharingplatform.repositories;

import lt.techin.recipesharingplatform.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {}
