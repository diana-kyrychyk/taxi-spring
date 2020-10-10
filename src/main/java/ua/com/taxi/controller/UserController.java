package ua.com.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.taxi.domain.dto.UserRegistrationDto;
import ua.com.taxi.service.UserService;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public String getLogin() {
        return "user-login";
    }

    @GetMapping("/guest/user-registration")
    public String getRegistrationForm(Model model) {
        UserRegistrationDto user = new UserRegistrationDto();
        model.addAttribute("user", user);
        return "/user/user-registration";
    }

    @PostMapping("/guest/user-registration")
    public String registerUser(@Validated @ModelAttribute("user") UserRegistrationDto user,
                               BindingResult bindingResult) {

        String page = "";
        if (bindingResult.hasErrors()) {
            page = "/user/user-registration";
        } else {
            userService.create(user);
            page = "redirect:/login";
        }
        return page;
    }
}
