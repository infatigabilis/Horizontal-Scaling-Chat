package otus.project.horizontal_scaling_chat.db.dataset;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class CommonChannel {
    @Expose private long id;
    @Expose private String name;

    public CommonChannel() {
    }

    public CommonChannel(long id, String name) {
        this.id = id;
        this.name = name;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return id == ((CommonChannel) obj).id;
    }
}
