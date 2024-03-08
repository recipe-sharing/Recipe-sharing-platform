package lt.techin.recipesharingplatform.controllers;

import lt.techin.recipesharingplatform.models.User;
import lt.techin.recipesharingplatform.security.SecurityConfig;
import lt.techin.recipesharingplatform.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = {"USER"})
    void createUser_whenCreateUser_thenReturnIt() throws Exception {
        //  given
        User user = new User(
                "abcdefghi.klmno49@efghijk.com",
                "Password123&=",
                "Smauglys87",
                "Vardas",
                "Pavarde",
                "Male",
                "ROLE_USER");
        given(this.userService.saveUser(any(User.class))).willReturn(user);

        // ObjectMapper om = new ObjectMapper();
        // om.findAndRegisterModules();

        //  when
        mockMvc.perform(
                        post("/register")
                        //       .contentType(MediaType.APPLICATION_JSON)
                        //       .accept(MediaType.APPLICATION_JSON)
                        // .content(om.writeValueAsString(user)))
                        )
                //  then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("abcdefghi.klmno49@efghijk.com"))
                // .andExpect(jsonPath("$.password").value("Password123&="))
                .andExpect(jsonPath("$.displayName").value("Smauglys87"))
                .andExpect(jsonPath("$.firstName").value("Vardas"))
                .andExpect(jsonPath("$.lastName").value("Pavarde"))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));

        verify(this.userService).saveUser(any(User.class));
    }

    // public PasswordEncoder passwordEncoder() {
    //        return new BCryptPasswordEncoder();

    @Test
    @WithMockUser
    void createUser_whenNotAllowed_returnBadRequest() throws Exception {
        //  given
        User user = new User(
                "abcdefghi.klmno49@efghijk.com",
                "Password123&=",
                "Smauglys87",
                "Vardas",
                "Pavarde",
                "Male",
                "ROLE_USER");
        given(this.userService.saveUser(any(User.class))).willReturn(user);

        //        ObjectMapper om = new ObjectMapper();
        //        om.findAndRegisterModules();

        //  when
        mockMvc.perform(
                        post("/register")
                        //                        .contentType(MediaType.APPLICATION_JSON)
                        //                        .accept(MediaType.APPLICATION_JSON)
                        //                        .content(om.writeValueAsString(user)))
                        )
                //  then
                .andExpect(status().isBadRequest());

        verify(this.userService, times(0)).saveUser(any(User.class));
    }
}