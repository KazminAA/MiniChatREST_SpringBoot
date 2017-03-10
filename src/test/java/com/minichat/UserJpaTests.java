package com.minichat;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.minichat.configs.JPAConfig;
import com.minichat.models.User;
import com.minichat.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {JPAConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("com/minichat/user-entries.xml")
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
}
