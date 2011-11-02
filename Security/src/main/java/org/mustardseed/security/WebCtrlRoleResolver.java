package org.mustardseed.security;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.lang.reflect.Method;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

import org.mustardseed.security.annotation.RoleAuthStamp;


/**
 *角色控制安全模块
 *@author HermitWayne
 *
 *<bean name="webCtrlRoleResolver"
 *	class="org.mustardseed.security.WebCtrlRoleResolver">
 *  <property name="roleAuthBean">
 *    <bean class="org.mustardseed.demo.RoleAuthenticationBean"/>
 *  </property>
 *</bean>
 */
public class WebCtrlRoleResolver
    implements WebCtrlAuthResolver {

    private RoleAuthenticationBean roleAuthBean;
    private String defaultErrorCode;
    
    public AuthError checkAuthentication(Class clazz,
					 Method method,
					 NativeWebRequest webRequest) {
	
	RoleAuthStamp[] rs = new RoleAuthStamp[] {
	    (RoleAuthStamp)clazz.getAnnotation(RoleAuthStamp.class),
	    (RoleAuthStamp)method.getAnnotation(RoleAuthStamp.class)};
	
	Set<String> role = 
	    getAccessRoles(roleAuthBean.getAuthRoles(), rs);
	
	String ret = roleAuthBean.checkAuth(role, webRequest);

	return getErrorCode(ret, rs);
    }

    private AuthError getErrorCode(String check, RoleAuthStamp[] roles) {
	AuthError ret = null;
	String tmp = check;
	
	if (!RoleAuthenticationBean.AUTH_OK.equals(tmp)) {
	    for (int i = 0; i < roles.length; i += 1)
		if (roles[i] != null && !roles[i].error().isEmpty())
		    tmp = roles[i].error();
	    if (defaultErrorCode != null && (tmp == null || tmp.isEmpty()))
		tmp = defaultErrorCode;
	    ret = new AuthError(AuthError.WebCtrlRole, tmp);
	}
	return ret;
    }
    
    private Set<String> getAccessRoles(Set<String> allRoles,
				       RoleAuthStamp[] roles) {
	Set<String> ret = new HashSet();
	ret.addAll(allRoles);

	for (int i = 0 ; i < roles.length; i += 1) {
	    RoleAuthStamp tmp = roles[i];
	    if (tmp == null) continue;
	    
	    if (tmp.value().length > 0)
		ret.retainAll(Arrays.asList(tmp.value()));
	    if (tmp.except().length > 0)
		ret.removeAll(Arrays.asList(tmp.except()));
	}
	    
	return ret;
    }

    public void setRoleAuthBean(RoleAuthenticationBean roleAuthBean) {
	this.roleAuthBean = roleAuthBean;
    }
    public void setDefaultErrorCode(String defaultErrorCode) {
	this.defaultErrorCode = defaultErrorCode;
    }
}