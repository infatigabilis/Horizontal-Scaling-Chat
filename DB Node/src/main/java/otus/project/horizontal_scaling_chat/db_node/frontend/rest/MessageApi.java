package otus.project.horizontal_scaling_chat.db_node.frontend.rest;

import otus.project.horizontal_scaling_chat.beans.BeanHelper;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.User;
import otus.project.horizontal_scaling_chat.db_node.frontend.JsonFrontend;
import otus.project.horizontal_scaling_chat.db_node.db.service.MessageService;
import otus.project.horizontal_scaling_chat.frontend.auth.Secured;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/{channel}/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageApi extends JsonFrontend {
    private final MessageService messageService = BeanHelper.getBean(MessageService.class);

    @GET
    @Secured(User.Role.USER)
    public String get(@PathParam("channel") long channelId, @QueryParam("page") int page, @Context SecurityContext ctx) {
        checkMembership(channelId, ctx.getUserPrincipal().getName());
        return respond(messageService.get(channelId, page));
    }
}
