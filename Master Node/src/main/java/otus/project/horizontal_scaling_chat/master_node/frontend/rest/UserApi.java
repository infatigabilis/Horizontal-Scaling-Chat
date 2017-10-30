package otus.project.horizontal_scaling_chat.master_node.frontend.rest;

import com.google.gson.Gson;
import otus.project.horizontal_scaling_chat.beans.BeanHelper;
import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;
import otus.project.horizontal_scaling_chat.frontend.auth.Secured;
import otus.project.horizontal_scaling_chat.master_node.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.master_node.db.dataset.User;
import otus.project.horizontal_scaling_chat.master_node.db.service.UserService;
import otus.project.horizontal_scaling_chat.master_node.frontend.JsonFrontend;
import otus.project.horizontal_scaling_chat.master_node.share.Sharer;
import otus.project.horizontal_scaling_chat.share.message.user.RefreshUserTokenMessage;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserApi extends JsonFrontend {
    private final UserService userService = BeanHelper.getBean(UserService.class);

    @GET @Path("/me")
    @Secured(User.Role.USER)
    public String me(@Context SecurityContext ctx) {
        return respond(userService.get(Long.parseLong(ctx.getUserPrincipal().getName())));
    }
}
