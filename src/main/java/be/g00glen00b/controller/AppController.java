package be.g00glen00b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AppController {

  @RequestMapping(method = RequestMethod.GET)
  public String viewApplication() {
    return "index";
  }
}
