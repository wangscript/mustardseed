package org.mustardseed.security;

import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Method;

import org.junit.*;
import static org.junit.Assert.*;

import org.springframework.web.context.request.NativeWebRequest;

import org.mustardseed.security.AuthError;
import org.mustardseed.security.WebCtrlRoleResolver;
import org.mustardseed.security.RoleAuthenticationBean;
import org.mustardseed.security.annotation.RoleAuthStamp;

@RoleAuthStamp(except={"B","E"}, error="class-error")
public class WebCtrlRoleResolverTest 
    implements RoleAuthenticationBean {
    private WebCtrlRoleResolver roleAuth;
    
    @Before
    public void init() {
	roleAuth = new WebCtrlRoleResolver();
	roleAuth.setRoleAuthBean(this);
    }
    
    @Test
    public void roleNull() {
	AuthError ae = null;
	ae = roleAuth.checkAuthentication(this.getClass(),
					  findMethod("roleNull")
					  , null);
	assertNull("Not NULL", ae);
    }
    @Test
    @RoleAuthStamp({"B","E"})
    public void roleBE() {
	AuthError ae = null;
	ae = roleAuth.checkAuthentication(this.getClass(),
					  findMethod("roleBE")
					  , null);
	assertNotNull(ae);
	assertEquals(AuthError.WebCtrlRole, ae.getError());
	assertEquals("class-error", ae.getErrorCode());
    }
    @Test
    @RoleAuthStamp(except={"C","H"})
    public void role_CH() {
	AuthError ae = null;
	ae = roleAuth.checkAuthentication(this.getClass(),
					  findMethod("role_CH")
					  , null);
	assertNotNull(ae);
	assertEquals(AuthError.WebCtrlRole, ae.getError());
	assertEquals("class-error", ae.getErrorCode());
    }
    @Test
    @RoleAuthStamp(except="C")
    public void role_C() {
	AuthError ae = null;
	ae = roleAuth.checkAuthentication(this.getClass(),
					  findMethod("role_C")
					  , null);
	assertNull("Not Null", ae);
    }
    @Test
    @RoleAuthStamp("H")
    public void roleH() {
	AuthError ae = null;
	ae = roleAuth.checkAuthentication(this.getClass(),
					  findMethod("roleH")
					  , null);
	assertNull("Not Null", ae);
    }
    @Test
    @RoleAuthStamp({"H","E"})
    public void roleHE() {
	AuthError ae = null;
	ae = roleAuth.checkAuthentication(this.getClass(),
					  findMethod("roleHE")
					  , null);
	assertNull("Not Null", ae);
    }
    @Test
    @RoleAuthStamp(value={"A", "B"}, error="method-error")
    public void roleAB() {
	AuthError ae = null;
	ae = roleAuth.checkAuthentication(this.getClass(),
					  findMethod("roleAB"),
					  null);
	assertNotNull(ae);
	assertEquals(AuthError.WebCtrlRole, ae.getError());
	assertEquals("method-error", ae.getErrorCode());
    }
    
    private Method findMethod(String name) {
	Method ret = null;
	try {
	    ret = this.getClass().getMethod(name);
	} catch (Exception e) {
	    fail("Method found error!");
	}
	assertNotNull("Method NULL", ret);	
	return ret;
    }

    //method for bean
    public Set<String> getAuthRoles() {
	Set<String> ret = new HashSet<String>();
	ret.add("A");
	ret.add("B");
	ret.add("C");
	ret.add("D");
	ret.add("F");
	ret.add("G");
	ret.add("H");
	return ret;
    }
    public String checkAuth(Set<String> roles, NativeWebRequest webRequest) {
	String ret = "";
	Set<String> auth = new HashSet();
	auth.add("C"); auth.add("H");
	auth.retainAll(roles);
	if (auth.size() > 0)
	    ret = AUTH_OK;
	return ret;
    }
}