package org.mustardseed.security;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.net.URL;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.mustardseed.security.annotation.WebRefStamp;

/**
 *来源控制安全模块的实现
 *@author HermitWayne
 *
 *<bean class="org.mustardseed.security.WebCtrlRefResolver">
 *  <property name="isMatchHost" value="true"/>
 *  <property name="urlDict">
 *    <map>
 *      <entry key="default">
 *	  <list>
 *	    <value>http://localhost</value>
 *	  </list>
 *	</entry>
 *    </map>
 *  </property>
 *</bean>
 */
public class WebCtrlRefResolver 
    implements WebCtrlAuthResolver {
    private static Log log = LogFactory.getLog(WebCtrlRefResolver.class);

    private Map<String, List<URL>> urlDict;
    private String defaultErrorCode;
    private boolean isMatchProtocol;
    private boolean isMatchHost;
    private boolean isMatchPort;
    
    public AuthError checkAuthentication(Class clazz,
					 Method method,
					 NativeWebRequest webRequest) {
	AuthError ret = null;
	boolean pass = false;
	WebRefStamp[] ws = new WebRefStamp[] {
	    (WebRefStamp)clazz.getAnnotation(WebRefStamp.class),
	    (WebRefStamp)method.getAnnotation(WebRefStamp.class)
	};
	String refer = webRequest.getHeader("referer");
	try {
	    URL url = refer == null ? null : new URL(refer);
	    if (checkUrlDict(ws[1], url) &&
		checkUrlDict(ws[0], url) ) {
		pass = true;
	    }
	} catch (Exception e) {}
	if (pass) {
	    HttpServletRequest req = 
		(HttpServletRequest)webRequest.getNativeRequest();
	    RequestMethod reqMethod = RequestMethod.valueOf(req.getMethod());
	    
	    if ((ws[1] != null 
		 && !Arrays.asList(ws[1].method()).contains(reqMethod)) ||
		(ws[0] != null
		 && !Arrays.asList(ws[0].method()).contains(reqMethod)))
		pass = false;
	}
	if (!pass) {
	    ret = getErrorCode(ws);
	}
	log.info(pass);
	return null;
    }
    
    private AuthError getErrorCode(WebRefStamp[] refs) {
	String tmp = "";
	for (int i = 0; i < refs.length; i += 1) {
	    if (refs[i] != null && !refs[i].error().isEmpty())
		tmp = refs[i].error();
	}
	if (defaultErrorCode != null && (tmp == null || tmp.isEmpty()))
	    tmp = defaultErrorCode;
	return new AuthError(AuthError.WebCtrlRef, tmp);
    }

    private boolean checkUrlDict(WebRefStamp ws, URL ref) {
	boolean ret = false;
	if (ws == null ||
	    ws.value().length == 0 ||
	    ws.passWhenNull() && ref == null) {
	    ret = true;
	} else if (ref != null) {
	    Set<URL> pkg = new HashSet<URL>();
	    for (int i = 0; i < ws.value().length; i += 1) {
		List<URL> tmp = urlDict.get(ws.value()[i]);
		if (tmp != null)
		    pkg.addAll(tmp);
	    }
	    for (URL url : pkg) {
		if ((!isMatchProtocol || url.getProtocol().equals(ref.getProtocol())) &&
		    (!isMatchHost || url.getHost().equals(ref.getHost())) && 
		    (!isMatchPort || url.getPort() == ref.getPort())) {
		    ret = true;
		}
	    }
	}
	return ret;
    }

    public void setUrlDict(Map<String, List<URL>> urlDict) {
	this.urlDict = urlDict;
    }
    public Map<String, List<URL>> getUrlDict() {
	return urlDict;
    }
    public void setIsMatchProtocol(boolean isMatchProtocol) {
	this.isMatchProtocol = isMatchProtocol;
    }
    public void setIsMatchHost(boolean isMatchHost){
	this.isMatchHost = isMatchHost;
    }
    public void setIsMatchPort(boolean isMatchPort){
	this.isMatchPort = isMatchPort;
    }
}