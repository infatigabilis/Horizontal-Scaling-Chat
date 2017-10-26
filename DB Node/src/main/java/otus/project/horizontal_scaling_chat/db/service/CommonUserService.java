package otus.project.horizontal_scaling_chat.db.service;

import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;

import java.util.Optional;

public interface CommonUserService {
    Optional<CommonUser> get(String token);
}
