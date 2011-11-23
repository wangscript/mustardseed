package org.mustardseed.script.javascript;

import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Context;

/**
 *@author HermitWayne
 */
public class DynamicContextFactory extends ContextFactory {
    protected boolean hasFeature(Context ctx, int featureIndex) {
	if (featureIndex == Context.FEATURE_DYNAMIC_SCOPE) 
	    return true;
	return super.hasFeature(ctx, featureIndex);
    }
}
