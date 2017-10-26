package otus.project.horizontal_scaling_chat.exception;

public class DBEnitytNotFoundException extends RuntimeException {
    public DBEnitytNotFoundException(Class entityClazz, Object id) {
        super(entityClazz.getName() + " by identificator " + id + " not found");
    }
}
