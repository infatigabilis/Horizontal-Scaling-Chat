package otus.project.horizontal_scaling_chat.master_node.db.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import otus.project.horizontal_scaling_chat.master_node.db.DBTest;
import otus.project.horizontal_scaling_chat.master_node.db.dataset.User;

import static org.junit.Assert.*;

public class UserServiceTest extends DBTest {
    @Autowired private UserService userService;
    @Autowired private ChannelService channelService;

    @After
    public void shutdown() {
        deleteAll();
    }

    @Test
    public void get() {
        userService.add(new User("1", "google", "login", "token"));
        assertTrue(userService.get("1", "google") != null);
    }

    @Test
    public void getByToken() {
        userService.add(new User("1", "google", "login", "token"));
        assertTrue(userService.get("token").get() != null);
    }

    @Test
    public void add() {
        userService.add(new User("1", "google", "login", "token"));
        User user = (User) userService.get("token").get();
        assertTrue(user != null);

        user.setToken("token2");
        userService.refreshToken(user);
        assertTrue(userService.get(user.getToken()).get() != null);
    }
}
