package com.example.serviceapp.common.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == 404 ) {
                return "error/404";
            } else if (statusCode == 403) {
                return "error/403";
            } else if (statusCode == 401) {
                return "error/401";
            }else if (statusCode == 400) {
                return "error/400";
            }
        }

        return "error/500";
    }
}
