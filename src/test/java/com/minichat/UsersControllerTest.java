package com.minichat;

import com.minichat.builders.UserBuilder;
import com.minichat.controllers.UsersController;
import com.minichat.models.User;
import com.minichat.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersControllerTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UsersController usersController;

    private MockMvc mockMvc;
    private UserBuilder userBuilder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = standaloneSetup(usersController).build();
        userBuilder = new UserBuilder();
    }

    @Test
    public void get204StatusTest_NoUsersInDatabase() throws Exception {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/users")).andExpect(status().isNoContent());
    }

    @Test
    public void correctGetTest_Status_MediaType_ReturnedObjects() throws Exception {
        when(userRepository.findAll()).thenReturn(createUsersList(2));
        mockMvc.perform(get("/users")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(0)))
                .andExpect(jsonPath("$[0].login", is("login0")))
                .andExpect(jsonPath("$[0].name", is("name0")))
                .andExpect(jsonPath("$[0].mail", is("mail0@mail.ru")))
                .andExpect(jsonPath("$[1].id", is(1)))
                .andExpect(jsonPath("$[1].login", is("login1")))
                .andExpect(jsonPath("$[1].name", is("name1")))
                .andExpect(jsonPath("$[1].mail", is("mail1@mail.ru")));

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void userByLogin_NotFound() throws Exception {
        when(userRepository.findUserByLogin("test")).thenReturn(null);
        mockMvc.perform(get("/users/test")).andExpect(status().isNotFound());
    }

    @Test
    public void userByLogin_Correct_Status_MedisType_ReturnedObject() throws Exception {
        when(userRepository.findUserByLogin("existingUser")).thenReturn(
                userBuilder.withId(33).withLogin("existingUser").withName("User1").build()
        );
        mockMvc.perform(get("/users/existingUser")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(33)))
                .andExpect(jsonPath("$.login", is("existingUser")))
                .andExpect(jsonPath("$.name", is("User1")));

        verify(userRepository, times(1)).findUserByLogin("existingUser");
        verifyNoMoreInteractions(userRepository);
    }

    private List<User> createUsersList(int count) {
        List<User> users = new ArrayList<>();
        User user;
        for (int i = 0; i < count; i++) {
            user = userBuilder
                    .withId(i)
                    .withLogin("login" + i)
                    .withName("name" + i)
                    .withMail("mail" + i + "@mail.ru")
                    .build();
            user.setId(i);
            users.add(user);
        }
        return users;
    }
}
