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
    public void getById() {
        userService.add(new User("1", "google", "login", "token"));
        User user = userService.get("1", "google");
        assertEquals(user, userService.get(user.getId()));
    }

    @Test
    public void getByToken() {
        userService.add(new User("1", "google", "login", "token"));
        userService.get("1", "google");
        assertTrue(userService.get("token").get() != null);
    }

    @Test
    public void refreshToken() {
        User user = new User("1", "google", "login", "token");
        userService.add(user);
        user = userService.get("1", "google");
        user.setToken("new_token");
        userService.refreshToken(user);
        assertEquals("new_token", userService.get(user.getSourceId(), user.getAuthSource()).getToken());
    }

    @Test
    public void add() {
        userService.add(new User("1", "google", "login", "token"));
        User user = (User) userService.get("token").get();
        assertTrue(user != null);

        user.setToken("token2");
        userService.refreshToken(user);
        User act = (User) userService.get(user.getToken()).get();
        assertTrue(userService.get(user.getToken()).get() != null);
    }
}
