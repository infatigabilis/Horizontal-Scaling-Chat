package otus.project.horizontal_scaling_chat.db_node.frontend;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import otus.project.horizontal_scaling_chat.beans.BeanHelper;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.User;
import otus.project.horizontal_scaling_chat.db_node.db.service.ChannelService;
import otus.project.horizontal_scaling_chat.db_node.db.service.UserService;
import otus.project.horizontal_scaling_chat.exception.DBEnitytNotFoundException;

import javax.ws.rs.ForbiddenException;

public class JsonFrontend {
    protected final ChannelService channelService = BeanHelper.getBean(ChannelService.class);
    protected final UserService userService = BeanHelper.getBean(UserService.class);

    protected String respond(Object obj) {
        return respond(obj, null);
    }

    protected String respond(Object obj, ExclusionStrategy strategy) {
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        if (strategy != null) gsonBuilder.addSerializationExclusionStrategy(strategy);

        Gson gson = gsonBuilder.create();
        return gson.toJson(obj);
    }

    protected void checkMembership(long channelId, String id) {
        Channel channel = channelService.get(channelId)
                .orElseThrow(() -> new DBEnitytNotFoundException(Channel.class, channelId));
        if (!channel.getMembers().contains(userService.get(Long.parseLong(id))))
            throw new ForbiddenException();
    }
}
