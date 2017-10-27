package otus.project.horizontal_scaling_chat.master_node.frontend.rest;

import com.google.gson.Gson;
import otus.project.horizontal_scaling_chat.beans.BeanHelper;
import otus.project.horizontal_scaling_chat.master_node.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.master_node.db.dataset.User;
import otus.project.horizontal_scaling_chat.master_node.db.service.UserService;
import otus.project.horizontal_scaling_chat.master_node.share.Sharer;
import otus.project.horizontal_scaling_chat.share.message.user.RefreshUserTokenMessage;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserApi {
    private final UserService userService = BeanHelper.getBean(UserService.class);

//  https://accounts.google.com/o/oauth2/v2/auth?client_id=764305920855-mvd0tmc97rvpn2mmgvr5r4b1th1t13a7.apps.googleusercontent.com&redirect_uri=http://localhost:8080/api/auth&scope=https://www.googleapis.com/auth/plus.me&response_type=code
    @GET
    public String auth(@QueryParam("code") String code) throws IOException, URISyntaxException {
        String accessToken = getAccessToken(code);
        User user = getGoogleUser(accessToken);

        User cur = userService.get(user.getSourceId(), user.getAuthSource());
        if (cur != null) {
            cur.setToken(user.getToken());
            userService.refreshToken(cur);

            List<Channel> channelList = cur.getChannels();
            cur.setChannels(null);
            for (Channel i : channelList)
                Sharer.send(i.getDbIndex(), new RefreshUserTokenMessage(cur));
        }
        else
            userService.add(user);

        return accessToken;
    }

    private String getAccessToken(String code) throws IOException {
        String url =
                "https://www.googleapis.com/oauth2/v4/token";

        String urlParam =
                "client_id=764305920855-mvd0tmc97rvpn2mmgvr5r4b1th1t13a7.apps.googleusercontent.com&" +
                        "client_secret=oMf2y5Wf4QBynRke8NoSnGvb&grant_type=authorization_code&" +
                        "redirect_uri=http://localhost:8080/api/auth&" +
                        "code=" + code;

        HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(urlParam);
            wr.flush();
        }

        int responseCode = con.getResponseCode();

        StringBuilder response = new StringBuilder();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        Token token = new Gson().fromJson(response.toString(), Token.class);

        return token.getAccess_token();
    }

    private User getGoogleUser(String accessToken) throws IOException {
        String url = "https://www.googleapis.com/plus/v1/people/me?access_token=" + accessToken;

        HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();

        StringBuilder response = new StringBuilder();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        GoogleProfile profile = new Gson().fromJson(response.toString(), GoogleProfile.class);

        return new User(profile.getId(), "google", profile.getDisplayName(), accessToken);
    }

    private class GoogleProfile {
        private String id;
        private String displayName;

        public GoogleProfile() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }

    private class Token {
        private String access_token;
        private int expires_in;

        public Token() {
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }
}
