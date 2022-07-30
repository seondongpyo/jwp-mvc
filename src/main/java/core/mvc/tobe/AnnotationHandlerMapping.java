package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.aspectj.util.Reflection;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ReflectionUtilsPredicates;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.Methods;
import static org.reflections.util.ReflectionUtilsPredicates.*;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
            .forEach(this::mappingHandlerExecutions);
    }

    private void mappingHandlerExecutions(Class<?> controller) {
        Set<Method> methods = ReflectionUtils.get(Methods.of(controller, withAnnotation(RequestMapping.class)));
        methods.forEach(method -> {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            handlerExecutions.put(
                new HandlerKey(annotation.value(), annotation.method()),
                new HandlerExecution(method)
            );
        });
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
