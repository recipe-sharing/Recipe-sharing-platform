package lt.techin.recipesharingplatform.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "Recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    @Column(name = "name")
    @NotEmpty(message = "Recipe name field cannot be empty")
    @Size(min = 3, max = 100, message = "Recipe name must be from 3 to 100 characters")
    private String recipeName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull
    @Size(min = 3, message = "Description cannot be shorter than 5 symbols")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private LocalDate localDate;

    public Recipe() {}

    public String getRecipeName() {
        return recipeName;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    //    public String getImage() {
    //        return image;
    //    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    //    public void setImage(String image) {
    //        this.image = image;
    //    }

    public long getId() {
        return id;
    }
}
