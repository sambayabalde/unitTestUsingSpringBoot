package com.eti.unitTesting.service;

import com.eti.unitTesting.entity.User;
import com.eti.unitTesting.repository.UserRepository;
import com.eti.unitTesting.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test des méthode de la classe UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService = new UserServiceImpl(userRepository);

    @Test
    public void test_addUser_whenSucess(){
        User user = new User(1L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");

        when(userRepository.save(user)).thenReturn(user);

        User userAdded = userService.save(user);

        assertThat(userAdded).isNotNull().isExactlyInstanceOf(User.class);
        assertThat(userAdded.getId()).isNotZero().isEqualTo(user.getId());
    }

    @Test
    public void test_addUser_when_EmailIsDuplicated(){
        User user1 = new User(1L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");

        when(userRepository.existsByEmail(user1.getEmail())).thenReturn(true);

        verify(userRepository, never()).save(user1);
        assertThrows(RuntimeException.class, ()->{
            userService.save(user1);
        });
    }

    @Test
    public void test_addUser_when_LoginUserisDuplicated(){

        User user1 = new User(1L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");

        when(userRepository.existsByLogin(user1.getLogin())).thenReturn(true);

        assertThrows(RuntimeException.class, ()->{
            userService.save(user1);
        });
    }

    @Test
    public void test_findUserById_whenIdUserExist() throws Exception {
        User user1 = new User(1L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        assertThat(userService.findById(1L)).isEqualTo(user1);
    }

    // Tests case for findById method
    @Test
    public void test_findUserById_whenIdUserNotExist() throws Exception {
        User user1 = new User(1L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");

        when(userRepository.findById(2L)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, ()->{
            userService.findById(2L);
        });
    }

    @Test
    public void test_findUserBy_whenIdUserIsNull() throws Exception {
        User user1 = new User(1L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");

        when(userRepository.findById(null)).thenThrow(NumberFormatException.class);

        assertThrows(NumberFormatException.class, ()->{
            userService.findById(null);
        });
    }

    @Test
    public void test_updateUser_when_UserIsOk(){
        User user = new User(1L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");
        User newUser = new User(user.getId(), "Mamadou Lamarana BALDE", user.getLogin(), user.getEmail(), "7654321");

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User userExpected = userService.update(newUser);

        assertThat(userExpected)
                .isNotNull()
                .isExactlyInstanceOf(User.class);
        assertThat(userExpected.getId()).isEqualTo(user.getId());
    }

    @Test
    public void test_updateUser_whenUserIdNotFound(){

        User user = new User(10L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");
        User newUser = new User(user.getId(), "Mamadou Lamarana BALDE", user.getLogin(), user.getEmail(), "7654321");

        when(userRepository.existsById(10L)).thenReturn(false);

        assertThrows(RuntimeException.class, ()->{
           userService.update(newUser);
        });
    }

  /*  @Test
    public void test_deleteUser_whenIdExist() throws Exception {

        User user = new User(1L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");
        //userRepository.save(user);

        when(userRepository.existsById(user.getId())).thenReturn(true);

        // when -  action or the behaviour that we are going test
        userRepository.deleteById(user.getId());
        Optional<User> userOptional = userRepository.findById(user.getId());

        // then - verify the output
        assertThat(userOptional).isEmpty();

    }*/

  @Test
  void test_deleteUser_whenUserExist() throws Exception {
      User user = new User(1L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");

      when(userRepository.existsById(user.getId())).thenReturn(true);

      userService.deleteById(user.getId());

      verify(userRepository, times(1)).existsById(user.getId());
      verify(userRepository, times(1)).deleteById(user.getId());
  }

    @Test
    void willThrowWhenDelete_WhenUserNotFound() throws Exception {
        // given
        long id = 10;
        when(userRepository.existsById(id)).thenReturn(false);
        // when
        verify(userRepository, never()).existsById(id);
        // then
        // vérifie que l'appel de deleteById(10) lève une exception de type RuntimeException
        assertThrows(RuntimeException.class, ()->{
            userService.deleteById(id);
        });
    }

    @Test
    public void test_givenUsersList_whenFindAll_thenUsersList(){
        // given - precondition or setup
        List<User> userList = new ArrayList<>();

        User user = new User(1L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");
        User user1 = new User(3L, "Mamadou Lamarana BALDE", "sambayabalde", "sambayabalde@eti.net.gn", "12345");

        userList.add(user);
        userList.add(user1);
        when(userRepository.findAll()).thenReturn(userList);

        // when -  action or the behaviour that we are going test
        List<User> userList1 = userService.findAll();

        // then - verify the output
        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    void findByEmailOrLogin_whenUserExist() {

        User user = new User(1L, "Nestor HABA", "nestor", "neshaba@eti.net.gn", "12345");

        when(userRepository.findAllByEmailOrLogin(user.getLogin())).thenReturn(user);

        User finded = userService.findByEmailOrLogin(user.getLogin());

        verify(userRepository, times(1)).findAllByEmailOrLogin(user.getLogin());
        assertThat(finded).isNotNull();
        assertThat(finded.getFullName()).isEqualTo(user.getFullName());

    }
}