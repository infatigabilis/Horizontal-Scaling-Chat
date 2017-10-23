package otus.project.horizontal_scaling_chat.db.dataset;

public interface CommonUser {
    long getId();
    String getSourceId();
    String getAuthSource();
    String getLogin();
    String getToken();

    public enum Role {
        USER
    }
}
