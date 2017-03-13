package com.minichat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({MessageJPATest.class, MessagesControllerTest.class,
        UserJpaTests.class, UsersControllerTest.class,
        SecurityConfigurationTest.class})
public class AllTests {
}
