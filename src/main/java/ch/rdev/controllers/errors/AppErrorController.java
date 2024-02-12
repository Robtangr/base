package ch.rdev.controllers.errors;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.logging.Logger;

@Controller
@ControllerAdvice
public class AppErrorController extends ResponseEntityExceptionHandler implements ErrorController {

    private static Logger LOG = Logger.getLogger(AppErrorController.class.getName());


    /***
     * Generic exception handler, fallback for all exceptions not handled by other handlers.
     * @param webRequest The web request
     * @param request The http servlet request
     * @return A response entity with the error information
     */
    @Order(Ordered.LOWEST_PRECEDENCE)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> output = new HashMap<>();
        String requestUri = request.getRequestURI();
        Exception exception = (Exception)request.getAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR");
        output.put("errorUri",requestUri);
        output.put("errorCode",500);
        output.put("errorMessage","Internal Server Error");
        output.put("errorDescription","The exception was handled by the generic exception handler.");
        if(exception == null){
            output.put("exception","The exception was not available in the servlet request.");
        } else {
            output.put("exception",exception.getMessage());
            StackTraceElement[] stackTrace = exception.getStackTrace();
            int count= 0;
            List<String> stack = new ArrayList<>();
            if(stackTrace != null){
                while(count < 10 && count < stackTrace.length) {
                    stack.add(stackTrace[count].toString());
                    count++;
                }
            }
            output.put("stackTrace", stack);
        }

        return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(output);
    }

    @RequestMapping("/error")
    public ResponseEntity<Map<String,Object>> handleError(WebRequest webRequest, HttpServletRequest request) {
        Map<String,Object> output = new HashMap<>();

        Integer errorCode = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorUri = (String)request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String errorMessage = (String)request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        if(errorCode == null){
            errorCode = 500;
            errorMessage = "Internal Server Error : The original error message was not available in the servlet request.";
        }
        output.put("errorCode",errorCode);
        output.put("errorUri",errorUri);
        output.put("errorMessage",errorMessage);
        return ResponseEntity.status(errorCode).body(output);
    }
}
