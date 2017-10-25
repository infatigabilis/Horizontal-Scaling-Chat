package otus.project.horizontal_scaling_chat.frontend.auth;

import otus.project.horizontal_scaling_chat.beans.BeanHelper;
import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;
import otus.project.horizontal_scaling_chat.db.service.CommonUserService;
import otus.project.horizontal_scaling_chat.exception.InvalidTokenException;
import otus.project.horizontal_scaling_chat.exception.WrongTokenException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Provider @Secured
public class AuthFilter implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;

    private final CommonUserService commonUserService = BeanHelper.getBean(CommonUserService.class);

    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token = getValidToken(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION));
        CommonUser commonUser = commonUserService.get(token).orElseThrow(WrongTokenException::new);
//        checkRole(commonUser);

        SecurityContext securityContext = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> "" + commonUser.getId();
            }

            @Override
            public boolean isUserInRole(String s) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return true;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        };

        requestContext.setSecurityContext(securityContext);
    }

    private String getValidToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.toLowerCase().startsWith("bearer "))
            throw new InvalidTokenException();

        return authorizationHeader.substring("Bearer".length()).trim();
    }

    private void checkRole(CommonUser commonUser) {
        Class<?> resourceClass = resourceInfo.getResourceClass();
        List<CommonUser.Role> classRoles = extractRoles(resourceClass);

        Method resourceMethod = resourceInfo.getResourceMethod();
        List<CommonUser.Role> methodRoles = extractRoles(resourceMethod);

        if (methodRoles.isEmpty()) {
            checkPermissions(classRoles, commonUser);
        } else {
            checkPermissions(methodRoles, commonUser);
        }
    }

    private List<CommonUser.Role> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<CommonUser.Role>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<CommonUser.Role>();
            } else {
                CommonUser.Role[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private void checkPermissions(List<CommonUser.Role> roles, CommonUser commonCommonUser) {
//        for (CommonUser.Role role : roles)
//            if (role.equals(commonCommonUser.getRole())) return;
//
//        throw new ForbiddenException();
    }
}
