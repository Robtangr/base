package ch.rdev.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping()
    @Hidden
    public String redirectIndex(Model mod){

        return "index";
    }

    @GetMapping("/swagger")
    @Hidden
    public RedirectView redirectSwagger(){
        return new RedirectView("swagger-ui.html");
    }


}
