package com.minichat;

import com.minichat.configs.SecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = {SecurityConfig.class})
@Transactional(rollbackOn = Exception.class)
public class SecurityConfigurationTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private Filter springSecurityFilterChain;
    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    public void authFail_ShouldReturnStatus() throws Exception {
        mvc.perform(get("/messages"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void authSuccess_ShouldreturnStatusOk() throws Exception {
        mvc.perform(get("/messages").with(user("user1").password("1")))
                .andExpect(SecurityMockMvcResultMatchers.authenticated().withUsername("user1"))
                .andExpect(status().isOk());
    }
}
