package otus.project.horizontal_scaling_chat.db.service;

import otus.project.horizontal_scaling_chat.db.dataset.User;

import java.util.Optional;

public interface CommonUserService {
    Optional<User> get(String token);
}
