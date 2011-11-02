package org.mustardseed.security;

import java.util.Set;
import org.springframework.web.context.request.NativeWebRequest;

/**
 *在角色访问控制中，用于让用户实现角色判定的对象的接口.
 *@author HermitWayne
 */
public interface RoleAuthenticationBean{
    public static String AUTH_OK = "ROLE_AUTH_BEAN_PASS";
    public Set<String> getAuthRoles();
    public String checkAuth(Set<String> roles, NativeWebRequest webRequest);
}