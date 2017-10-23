package otus.project.horizontal_scaling_chat.master_node.frontend;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonFrontend {
    protected String respond(Object obj) {
        return respond(obj, null);
    }

    protected String respond(Object obj, ExclusionStrategy strategy) {
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        if (strategy != null) gsonBuilder.addSerializationExclusionStrategy(strategy);

        Gson gson = gsonBuilder.create();
        return gson.toJson(obj);
    }
}
