package com.minichat;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.minichat.builders.MessageBuilder;
import com.minichat.builders.UserBuilder;
import com.minichat.configs.JPAConfig;
import com.minichat.models.Message;
import com.minichat.models.User;
import com.minichat.repositories.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
@DatabaseSetup(value = "classpath:messageEntries.xml")
public class MessageJPATest {

    @Autowired
    MessageRepository messageRepository;

    @Test
    public void findLast20MessagesSuccessfully() {
        PageRequest request = new PageRequest(0, 20, Sort.Direction.DESC, "id");
        List<Message> messageList = messageRepository.findWithPageable(request);
        assertThat(messageList).hasSize(4);
        Message message = messageList.get(1);
        assertThat(message.getMessageText()).contains("Давай");
    }

    @Test
    public void addNewMessageSuccessfully() {
        User user = new UserBuilder().withId(12).build();
        Message message = new MessageBuilder().withId(0).withUser(user).build();
        assertThat(message.getId() == 0);
        messageRepository.saveAndFlush(message);
        assertThat(message.getId() != 0);
    }
}
