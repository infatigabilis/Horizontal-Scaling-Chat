package otus.project.horizontal_scaling_chat.master_node.frontend.auth;

import otus.project.horizontal_scaling_chat.frontend.auth.AuthFilter;
import otus.project.horizontal_scaling_chat.frontend.auth.Secured;

import javax.ws.rs.ext.Provider;

@Provider @Secured
public class AuthFilterImpl extends AuthFilter {

}
