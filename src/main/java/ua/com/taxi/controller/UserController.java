package ua.com.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.taxi.domain.Role;
import ua.com.taxi.domain.User;
import ua.com.taxi.domain.dto.UserRegistrationDto;
import ua.com.taxi.domain.dto.UserUpdateDto;
import ua.com.taxi.service.RoleService;
import ua.com.taxi.service.UserService;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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

    @GetMapping("/admin/user-list")
    public String getUserList(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/admin/user-edit")
    public String updateUserForm(@RequestParam("id") int id, Model model) {
        UserUpdateDto user = userService.findForUpdate(id);
        List<Role> roles = roleService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roles);
        return "user/user-edit";
    }

    @PostMapping("/admin/user-edit")
    public String updateUser(@Validated @ModelAttribute("user") UserUpdateDto user,
                             BindingResult bindingResult) {

        String page = "";
        if (bindingResult.hasErrors()) {
            page = "/user/user-edit";
        } else {
            userService.update(user);
            page = "redirect:/admin/user-list";
        }
        return page;
    }
}
