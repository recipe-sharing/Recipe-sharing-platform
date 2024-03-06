package lt.techin.recipesharingplatform.controllers;

import jakarta.validation.Valid;
import lt.techin.recipesharingplatform.models.User;
import lt.techin.recipesharingplatform.repositories.UserRepository;
import lt.techin.recipesharingplatform.services.UserService;
import lt.techin.recipesharingplatform.validation.PasswordValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {

        PasswordValidator password = new PasswordValidator(user);
        String message = password.validatePassword();
        if (!message.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        User savedUser = this.userService.saveUser(user);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedUser.getId())
                        .toUri())
                .body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());

        if (userOptional.isPresent()) {
            User userDb = userOptional.get();

            if (passwordEncoder.matches(user.getPassword(), userDb.getPassword())) {
                // Authentication successful
                return ResponseEntity.ok().body("{\"message\": \"Login successful\"}");
            }
        }

        // Authentication failed
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Incorrect username or password\"}");
    }
}
