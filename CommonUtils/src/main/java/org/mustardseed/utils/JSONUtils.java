package org.mustardseed.utils;

import java.io.StringWriter;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *用于处理JSON的工具类
 *@author HermitWayne
 */
public class JSONUtils {
    private static ObjectMapper m = new ObjectMapper();
    private static JsonFactory jf = new JsonFactory();

    /**
     *将json数据填充至对象中。
     */
    public static <T> Object fromJson(String jsonAsString, 
				      Class<T> pojoClass) {
	try {
	    return m.readValue(jsonAsString, pojoClass);
	} catch (Exception e) {
	    return null;
	}
    }

    /**
     *将对象转换成json
     */
    public static String toJson(Object pojo) {
        StringWriter sw = new StringWriter();
	try {
	    JsonGenerator jg = jf.createJsonGenerator(sw);
	    m.writeValue(jg, pojo);
	} catch (Exception e) {}
        return sw.toString();
    }
}