package com.eti.unitTesting.repository;

import com.eti.unitTesting.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    UserRepository repoUnderTest;

    User user;

    @AfterEach
    void tearDown() {
        repoUnderTest.deleteAll();
    }

    @BeforeEach
    void downEach(){
        this.user = new User(1L,
                "Nestor HABA",
                "nestor",
                "neshaba@eti.net.gn",
                "12345");
    }

    @Test
    void test_findUserByEmailOrLogin_whenGivenLogin() {

        User userSaved = repoUnderTest.save(user);

        User userFinded = repoUnderTest.findAllByEmailOrLogin(userSaved.getLogin());

        Assertions.assertThat(userFinded).isNotNull();
        Assertions.assertThat(userFinded.getLogin()).isEqualTo(user.getLogin());
    }

    @Test
    void test_findUserByLoginOrEmail_whenGivenEmail() {


        User userSaved = repoUnderTest.save(user);

        User userFinded = repoUnderTest.findAllByEmailOrLogin(userSaved.getEmail());

        Assertions.assertThat(userFinded).isNotNull();
        Assertions.assertThat(userFinded.getEmail()).isEqualTo(user.getEmail());

    }

    @Test
    void test_findUserByEmailOrLogin_whenUserNotExist(){
        String email = "lamarana@gmail.com";

        User found = repoUnderTest.findAllByEmailOrLogin(email);

        Assertions.assertThat(found).isNull();
    }

    @Test
    void existsByLogin_whenUserExist(){
         String login = user.getLogin();

         repoUnderTest.save(user);

         boolean expected = repoUnderTest.existsByLogin(login);

         Assertions.assertThat(expected).isTrue();
    }

    @Test
    void existsByLogin_whenUserNotExist(){
        String login = "login1";

        boolean expected = repoUnderTest.existsByLogin(login);

        Assertions.assertThat(expected).isFalse();
    }

    @Test
    void existsByEmail_whenUserExist(){
        String login = user.getEmail();

        repoUnderTest.save(user);

        boolean expected = repoUnderTest.existsByEmail(login);

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    void existsByEmail_whenUserNotExist(){
        String login = "nes@gmail.com";

        boolean expected = repoUnderTest.existsByEmail(login);

        Assertions.assertThat(expected).isFalse();
    }
}