package com.springit.flowers.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.jexl3.*;
import org.apache.commons.jexl3.introspection.JexlPermissions;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class JexlParserUtils {

    private final JexlEngine jexl;

    static {
        Map<String, Object> functions = new HashMap<>();
        functions.put(null, CustomFunctionUtils.class);

        final JexlPermissions permissions = JexlPermissions.parse("com.springit.flowers.util.*");
        jexl = new JexlBuilder()
                .permissions(permissions)
                .namespaces(functions)
                .create();
    }

    public static boolean evaluate(String expression, Map<String, Object> params) {
        if (expression == null) {
            return true;
        }
        JexlExpression jexlExp = jexl.createExpression(expression);
        JexlContext context = new MapContext();
        if (params != null) {
            params.forEach(context::set);
        }
        return (Boolean) jexlExp.evaluate(context);
    }


}
