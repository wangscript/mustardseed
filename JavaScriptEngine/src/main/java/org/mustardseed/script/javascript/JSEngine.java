package org.mustardseed.script.javascript;

import java.io.Reader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;

/**
 *Javascript执行引擎封装
 *@author HermitWayne
 */
public class JSEngine 
    implements InitializingBean,
	       DisposableBean {
    
    private JSEngine baseEngine;
    private Context globalContext;
    private Scriptable sharedScope;
    
    //method for bean
    public void afterPropertiesSet()
	throws Exception {
	globalContext = Context.enter();
	if (baseEngine != null && baseEngine != this) {
	    sharedScope = baseEngine.initStandardObjects();
	} else {
	    baseEngine = null;
	    ContextFactory.initGlobal(new DynamicContextFactory());
	    sharedScope = globalContext.initStandardObjects(null, true);
	}
    }

    public void destroy()
	throws Exception {
	Context.exit();
    }

    //method for engine
    public Scriptable initStandardObjects() {
	Context cur = Context.getCurrentContext();
	Scriptable ret = cur.newObject(sharedScope);
	ret.setPrototype(sharedScope);
	ret.setParentScope(null);
	return ret;
    }
    public Script compile(String path) 
	throws java.io.IOException {
	
	Reader in = 
	    new InputStreamReader(new FileInputStream(path), 
				  "UTF-8");
	return compile(in, path);
    }	
    public Script complie(Reader in) 
	throws java.io.IOException {
	return compile(in, in.toString());
    }
    public Script compile(Reader in, String name) 
	throws java.io.IOException {
	Context ctx = Context.getCurrentContext();
	return ctx.compileReader(in, name, 0, null);
    }
    public Object exec(Script script, Scriptable scope) {
	Context ctx = Context.getCurrentContext();
	return script.exec(ctx, scope);
    }
    public Object callMethod(Scriptable scope,
			     String funcName, 
			     Object ... args) {
	Context ctx = Context.getCurrentContext();
	return ScriptableObject.callMethod(ctx, scope, 
					   funcName, args);
    }
    
    //getter and setter
    public void setBaseEngine(JSEngine baseEngine) {
	this.baseEngine = baseEngine;
    }
}