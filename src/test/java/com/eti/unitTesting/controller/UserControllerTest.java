package com.eti.unitTesting.controller;

import com.eti.unitTesting.entity.User;
import com.eti.unitTesting.help.Utils;
import com.eti.unitTesting.repository.UserRepository;
import com.eti.unitTesting.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest()
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    public void testAddUser() throws Exception {

        User user = new User(1L, "Lamarana BALDE", "sambaya", "lamar@gmail.com", "pass123");

        when(userService.save(any(User.class))).thenReturn(user);

        String url = "/user/save";
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.convertObjectToJsonBytes(user)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).save(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testFindById() throws Exception {
        User user = new User(1L, "Lamarana BALDE", "sambaya", "lamar@gmail.com", "pass123");
        when(userService.findById(user.getId())).thenReturn(user);
        String url = "/user/find-one/{id}";

        mockMvc.perform(get(url, 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is(user.getFullName())))
                .andExpect(jsonPath("$.login", is(user.getLogin())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));

        verify(userService, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testFindAll() throws Exception {
        // given
        User user1 = new User(1L, "Nestor HABA", "Gupta", "nestor@gmail.com", "pass12345");
        User user2 = new User(2L, "Aboubacar DIALLO", "Gussin", "abbcr@gmail.com", "pass123");

        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));
        String getAllUrl = "/user/find-all";

        MvcResult mockMvcResult = mockMvc.perform(get(getAllUrl).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andReturn();

        // then
        verify(userService, times(1)).findAll();
        assertThat(mockMvcResult.getResponse().getContentAsString()).contains("Nestor HABA");
        assertThat(mockMvcResult.getResponse().getContentAsString())
                .isNotEqualTo("");
    }

    @Test
    public void test_UpdateUser() throws Exception {
        User user = new User(1L, "Lamarana BALDE", "sambaya", "lamar@gmail.com", "pass123");
        user.setFullName("Mamadou Lamarana BALDE");

        when(userService.findById(user.getId())).thenReturn(user);
        when(userService.update(user)).thenReturn(user);
        String uri = "/user/update";

        mockMvc.perform(put(uri).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.convertObjectToJsonBytes(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("Mamadou Lamarana BALDE")))
                .andExpect(jsonPath("$.login", is(user.getLogin())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));
    }

    @Test
    public void test_DeleteUser() throws Exception {
        User user = new User(1L, "Lamarana BALDE", "sambaya", "lamar@gmail.com", "pass123");

        String uri = "/user/delete/{id}";

        mockMvc.perform(delete(uri, 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteById(user.getId());
    }

    @Test
    public void testFindByEmailOrLogin_whenGivenLogin() throws Exception {
        User user = new User(1L, "Lamarana BALDE", "sambaya", "lamar@gmail.com", "pass123");
        when(userService.findByEmailOrLogin(user.getLogin())).thenReturn(user);
        String url = "/user/search-by-emailOrLogin/{emailOrLogin}";

        mockMvc.perform(get(url, user.getLogin()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is(user.getFullName())))
                .andExpect(jsonPath("$.login", is(user.getLogin())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));

        verify(userService, times(1)).findByEmailOrLogin(user.getLogin());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testFindByEmailOrLogin_whenGivenEmail() throws Exception {
        User user = new User(1L, "Lamarana BALDE", "sambaya", "lamar@gmail.com", "pass123");
        when(userService.findByEmailOrLogin(user.getEmail())).thenReturn(user);
        String url = "/user/search-by-emailOrLogin/{emailOrLogin}";

        mockMvc.perform(get(url, user.getEmail()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is(user.getFullName())))
                .andExpect(jsonPath("$.login", is(user.getLogin())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));

        verify(userService, times(1)).findByEmailOrLogin(user.getEmail());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testFindByEmailOrLogin_whenGivenAnyString() throws Exception {
        User user = new User(1L, "Lamarana BALDE", "sambaya", "lamar@gmail.com", "pass123");
        when(userService.findByEmailOrLogin(user.getEmail())).thenReturn(user);
        String url = "/user/search-by-emailOrLogin/{emailOrLogin}";

         MvcResult mvcResult = mockMvc.perform(get(url, "test-string").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
                //.andExpect(jsonPath("$", Matchers.nullValue()));

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("");

        verify(userService, times(1)).findByEmailOrLogin("test-string");
        verifyNoMoreInteractions(userRepository);
    }
}
