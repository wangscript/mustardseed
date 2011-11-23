package org.mustardseed.validation;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import org.springframework.web.context.ServletContextAware;
import org.mustardseed.script.javascript.JSEngine;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.RhinoException;

/**
 *@author HermitWayne
 */
public class Validator 
    extends JSEngine 
    implements ServletContextAware {

    public static String VALID_ERR = "VALIDATOR_ERROR_MESSAGE";

    private ServletContext servletContext;
    private String scriptPath;
    
    public Map<String, String[]> validate(String method,
					  Map<String, String[]> params) {
	return validate(scriptPath, method, params);
    }
    public Map<String, String[]> validate(String script,
					  String method,
					  Map<String, String[]> params) {
	Map<String, String[]> ret = null;
	Context ctx = Context.enter();
	try {
	    
	    Scriptable scope = initStandardObjects();
	    Scriptable table = transJavaToJs(params);
	    exec(compile(transScriptPath(script)), scope);
	    Object tmp = callMethod(scope, method, table);
	    if (tmp instanceof Scriptable) {
		ret = transJsToJava((Scriptable)tmp);
	    }
	} catch (RhinoException e) {
	    ret = new HashMap<String, String[]>();
	    ret.put(VALID_ERR, 
		    new String[]{String.format("[ERROR: %s]\nLINE: %d\n%s", 
					       e.sourceName(),
					       e.lineNumber(),
					       e.details()
					       )});
	} catch (Exception e) {
	    ret = new HashMap<String, String[]>();
	    ret.put(VALID_ERR, new String[]{"[ERROR: UNKNOWN]"});
	} finally {
	    Context.exit();
	}
	return ret;
    }
    
    private Scriptable transJavaToJs(Map<String, String[]> params) {
	Scriptable table = new NativeObject();
	for (Map.Entry<String, String[]> entry : params.entrySet()) {
	    String[] lst = entry.getValue();
	    Scriptable tmp = new NativeArray(lst.length);
	    for (int i = 0; i < lst.length; i += 1) {
		tmp.put(i, tmp, ""+lst[i]);
	    }
	    
	    table.put(entry.getKey(),table, tmp);
	}
	return table;
    }
    private Map<String, String[]>transJsToJava(Scriptable table) {
	Map<String, String[]> ret = new HashMap<String, String[]>();
	
	for(Object i : table.getIds()) {
	    List<String> tmp = new ArrayList();
	    Scriptable item = (Scriptable)table.get((String)i, table);
	    for(Object j : item.getIds()) {
		tmp.add((String)item.get((Integer)j, item));
	    }
	    if (tmp.size() > 0)
		ret.put((String)i, tmp.toArray(new String[0]));
	    
	}
	if (ret.size() == 0)
	    ret = null;
	return ret;
    }
    private String transScriptPath(String path) {
	String ret = path;
	try {
	    ret = servletContext.getRealPath(path);
	} catch (Exception e) {}
	return ret;
    }
    
    public void setScriptPath(String scriptPath) {
	this.scriptPath = scriptPath;
    }
    public void setServletContext(ServletContext servletContext) {
	this.servletContext = servletContext;
    }
}