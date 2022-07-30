package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            Object handler = method.getDeclaringClass().getDeclaredConstructor().newInstance();
            return (ModelAndView) method.invoke(handler, request, response);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
