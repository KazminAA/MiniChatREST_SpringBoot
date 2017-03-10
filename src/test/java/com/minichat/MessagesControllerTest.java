package com.minichat;

import com.minichat.builders.MessageBuilder;
import com.minichat.controllers.MessagesController;
import com.minichat.models.Message;
import com.minichat.repositories.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MessagesControllerTest {
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessagesController messagesController;

    private MockMvc mvc;
    private MessageBuilder messageBuilder = new MessageBuilder();
    @Autowired
    private WebApplicationContext wac;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mvc = standaloneSetup(messagesController).build();
    }

    @Test
    public void messageByCount() throws Exception {
        PageRequest pageRequest = new PageRequest(0, 3, Sort.Direction.DESC, "id");
        when(messageRepository.findWithPageable(pageRequest)).thenReturn(TestsHelpers.createMessagesList(3));
        mvc.perform(get("/messages/3"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].messageText", is("Message 1")))
                .andExpect(jsonPath("$[2].messageText", is("Message 3")));
        verify(messageRepository, times(1)).findWithPageable(pageRequest);
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    public void getMethodTest() throws Exception {
        PageRequest pageRequest = new PageRequest(0, 20, Sort.Direction.DESC, "id");
        when(messageRepository.findWithPageable(pageRequest)).thenReturn(TestsHelpers.createMessagesList(20));
        mvc.perform(get("/messages"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(20)))
                .andExpect(jsonPath("$[0].messageText", is("Message 1")))
                .andExpect(jsonPath("$[19].messageText", is("Message 20")));
        verify(messageRepository, times(1)).findWithPageable(pageRequest);
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    public void postMethodTest() throws Exception {
        Message message = messageBuilder.withTime(null).build();
        mvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestsHelpers.getObjectAsJsonBytes(message)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("http://localhost/messages")));
    }
}
