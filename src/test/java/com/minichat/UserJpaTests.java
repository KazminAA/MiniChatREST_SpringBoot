package com.minichat;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.minichat.builders.UserBuilder;
import com.minichat.configs.JPAConfig;
import com.minichat.models.User;
import com.minichat.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ContextConfiguration(classes = {JPAConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup(value = "classpath:messageEntries.xml", type = DatabaseOperation.CLEAN_INSERT)
public class UserJpaTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void findAllTest_ShouldReturn3Entries() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(3);
        User found1 = users.get(1);
        assertThat(found1.getId()).isEqualTo(13);
    }

    @Test
    public void addUserSuccessfulll() {
        User user = new UserBuilder().withId(0).build();
        assertThat(user.getId() == 0);
        userRepository.saveAndFlush(user);
        assertThat(user.getId() != 0);
    }
}
