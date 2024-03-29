package lt.techin.recipesharingplatform.controllers;

import jakarta.validation.Valid;
import lt.techin.recipesharingplatform.models.Category;
import lt.techin.recipesharingplatform.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> saveCategory(@Valid @RequestBody Category category) {
        if (categoryService.existsByName(category.getName())) {
            Map<String, Object> response = new HashMap<>();
            response.put("name", "Category with this name already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Category savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedCategory.getId())
                        .toUri())
                .body(savedCategory);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}