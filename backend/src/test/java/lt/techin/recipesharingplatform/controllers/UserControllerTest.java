package lt.techin.recipesharingplatform.controllers;

import lt.techin.recipesharingplatform.models.User;
import lt.techin.recipesharingplatform.security.SecurityConfig;
import lt.techin.recipesharingplatform.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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

    private UserController userController;

    @Test
    void createUser_whenCreateUser_thenReturnIt() throws Exception {
        //  given
        User user = new User("email@email.com", "Password=123", "Smauglys87", "Vardas", "Pavarde", "Male");
        given(this.userService.saveUser(any(User.class))).willReturn(user);

        //  when
        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                         {
                                                             "email": "email@email.com",
                                                             "password": "Password=123",
                                                             "displayName": "Smauglys87",
                                                             "firstName": "Vardas",
                                                             "lastName": "Pavarde",
                                                             "gender": "Male"
                                                         }
                                                         """))

                //  then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("email@email.com"))
                .andExpect(jsonPath("$.displayName").value("Smauglys87"))
                .andExpect(jsonPath("$.firstName").value("Vardas"))
                .andExpect(jsonPath("$.lastName").value("Pavarde"))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(this.userService).saveUser(any(User.class));
    }

    @Test
    void createUser_whenNotAllowed_returnBadRequest() throws Exception {
        //  given
        User user =
                new User("abcdefghi.klmno49@efghijk.com", "Password=123", "Smauglys87", "Vardas", "Pavarde", "Male");
        given(this.userService.saveUser(any(User.class))).willReturn(user);

        //  when
        mockMvc.perform(post("/register"))

                // then
                .andExpect(status().isBadRequest());

        verify(this.userService, times(0)).saveUser(any(User.class));
    }

    @Test
    void createUser_whenDisplayNameExists_thenReturnBadRequest() throws Exception {
        //  given
        given(this.userService.existsUserByDisplayName("Display1")).willReturn(true);

        //  when
        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                                         {
                                                                             "displayName": "Display1",
                                                                             "email": "abcdefghi.klmno49@efghijk.com",
                                                                             "password": "Password=1",
                                                                             "firstName": "Vardas",
                                                                             "lastName": "Pavarde",
                                                                             "gender": "Female"
                                                                         }
                                                                         """))

                //  then
                .andExpect(status().isBadRequest());

        verify(this.userService, times(0)).saveUser(any(User.class));
        verify(this.userService).existsUserByDisplayName("Display1");
    }

    @Test
    void createUser_whenEmailExists_thenReturnBadRequest() throws Exception {
        //  given
        given(this.userService.existsUserByEmail("email@email.com")).willReturn(true);

        //  when
        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                                         {
                                                                             "displayName": "Display1",
                                                                             "email": "email@email.com",
                                                                             "password": "Password=1",
                                                                             "firstName": "Vardas",
                                                                             "lastName": "Pavarde",
                                                                             "gender": "Female"
                                                                         }
                                                                         """))

                //  then
                .andExpect(status().isBadRequest());

        verify(this.userService, times(0)).saveUser(any(User.class));
        verify(this.userService).existsUserByEmail("email@email.com");
    }

    @Test
    void createUser_whenEmailNotExists_thenReturnIsCreated() throws Exception {
        //  given
        given(this.userService.existsUserByEmail("email@email.com")).willReturn(false);

        //  when
        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                                         {
                                                                             "displayName": "Display1",
                                                                             "email": "email@email.com",
                                                                             "password": "Password=1",
                                                                             "firstName": "Vardas",
                                                                             "lastName": "Pavarde",
                                                                             "gender": "Female"
                                                                         }
                                                                         """))

                //  then
                .andExpect(status().isCreated());

        verify(this.userService).saveUser(any(User.class));
        verify(this.userService).existsUserByEmail("email@email.com");
    }
}
