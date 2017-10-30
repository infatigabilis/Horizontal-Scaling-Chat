package otus.project.horizontal_scaling_chat.share;

import com.google.gson.Gson;
import sun.misc.IOUtils;

import java.io.*;
import java.util.Scanner;

public class MasterEndpoint {
    private String host;
    private int port;

    public MasterEndpoint() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static MasterEndpoint[] get(String file) {
        try (InputStream input = MasterEndpoint.class.getClassLoader().getResourceAsStream(file)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));

            StringBuilder builder = new StringBuilder();
            String line;
            while((line = br.readLine()) != null)
                builder.append(line);

            return new Gson().fromJson(builder.toString(), MasterEndpoint[].class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
