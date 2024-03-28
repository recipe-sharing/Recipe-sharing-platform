package lt.techin.recipesharingplatform.controllers;

import jakarta.validation.Valid;
import lt.techin.recipesharingplatform.models.Recipe;
import lt.techin.recipesharingplatform.models.User;
import lt.techin.recipesharingplatform.services.RecipeService;
import lt.techin.recipesharingplatform.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final UserService userService;

    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> saveRecipe(@Valid @RequestBody Recipe recipe) {
        User currentUser = getCurrentUser();
        System.out.println(currentUser);

        recipe.setUser(currentUser);

        Recipe savedRecipe = recipeService.saveRecipe(recipe);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedRecipe.getId())
                        .toUri())
                .body(savedRecipe);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> userOptional = userService.findUserByEmail(email);
        return userOptional.orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
