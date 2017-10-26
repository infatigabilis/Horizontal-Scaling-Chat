package otus.project.horizontal_scaling_chat.share;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TransmittedData {
    private String className;

    public TransmittedData() {
        this.className = this.getClass().getName();
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

    public static TransmittedData fromJson(String json) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
            String className = (String) jsonObject.get("className");
            Class<?> msgClass = Class.forName(className);
            return (TransmittedData) new Gson().fromJson(json, msgClass);
        } catch (ClassNotFoundException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
