package otus.project.horizontal_scaling_chat.master_node.frontend.rest;

import org.apache.ibatis.annotations.Select;
import otus.project.horizontal_scaling_chat.beans.BeanHelper;
import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;
import otus.project.horizontal_scaling_chat.exception.DBEnitytNotFoundException;
import otus.project.horizontal_scaling_chat.frontend.auth.Secured;
import otus.project.horizontal_scaling_chat.master_node.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.master_node.db.service.UserService;
import otus.project.horizontal_scaling_chat.master_node.frontend.JsonFrontend;
import otus.project.horizontal_scaling_chat.master_node.db.service.ChannelService;
import otus.project.horizontal_scaling_chat.master_node.share.Sharer;
import otus.project.horizontal_scaling_chat.share.message.channel.AddMemberMessage;
import otus.project.horizontal_scaling_chat.share.message.channel.CreateChannelMessage;
import otus.project.horizontal_scaling_chat.share.message.channel.ExpelMemberMessage;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

@Path("/channels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChannelApi extends JsonFrontend {
    private final ChannelService channelService = BeanHelper.getBean(ChannelService.class);
    private final UserService userService = BeanHelper.getBean(UserService.class);

    @GET
    public String get(@QueryParam("page") int page) {
        return respond(channelService.get(page));
    }

    @GET @Path("/my")
    @Secured(CommonUser.Role.USER)
    public String getByUser(@Context SecurityContext ctx) {
        return respond(channelService.getByUser(Long.parseLong(ctx.getUserPrincipal().getName())));
    }

    @POST
    @Secured(CommonUser.Role.USER)
    public Response create(Channel channel, @Context SecurityContext ctx) throws IOException {
        String host = Sharer.pickDB();
        channel.setHost(host);

        long userId = Long.parseLong(ctx.getUserPrincipal().getName());
        channelService.create(channel, userId);
        channel = channelService.getCur(channel);

        Sharer.send(host, new CreateChannelMessage(channel, userService.get(userId)));

        return Response.ok().build();
    }

    @PUT @Path("{channel}")
    @Secured(CommonUser.Role.USER)
    public Response joinToChannel(@PathParam("channel") long channelId, @Context SecurityContext ctx) throws IOException {
        Channel channel = channelService.getById(channelId)
                .orElseThrow(() -> new DBEnitytNotFoundException(Channel.class, channelId));

        long userId = Long.parseLong(ctx.getUserPrincipal().getName());
        channelService.addMember(channelId, userId);

        Sharer.send(channel.getHost(), new AddMemberMessage(channelId, userService.get(userId)));

        return Response.ok().build();
    }

    @DELETE @Path("{channel}")
    @Secured(CommonUser.Role.USER)
    public Response quitChannel(@PathParam("channel") long channelId, @Context SecurityContext ctx) throws IOException {
        Channel channel = channelService.getById(channelId)
                .orElseThrow(() -> new DBEnitytNotFoundException(Channel.class, channelId));

        long userId = Long.parseLong(ctx.getUserPrincipal().getName());
        channelService.expelMember(channelId, userId);

        Sharer.send(channel.getHost(), new ExpelMemberMessage(channel.getId(), userId));

        return Response.ok().build();
    }
}
