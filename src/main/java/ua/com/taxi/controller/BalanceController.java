package ua.com.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.taxi.domain.dto.user.RechargeBalance;
import ua.com.taxi.service.UserService;

import java.security.Principal;

@Controller
public class BalanceController {

    private UserService userService;

    @Autowired
    public BalanceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/balance-recharge")
    public String getBalanceForm(Model model) {
        RechargeBalance balance = new RechargeBalance();
        model.addAttribute("rechargeBalance", balance);
        return "user/balance-recharge";
    }

    @PostMapping("/user/balance-recharge")
    public String updateUserBalance(@Validated @ModelAttribute("rechargeBalance") RechargeBalance rechargeRequest,
                                    BindingResult bindingResult, Principal principal) {

        String page = "";
        if (bindingResult.hasErrors()) {
            page = "/user/balance-recharge";
        } else {
            userService.rechargeBalance(principal.getName(), rechargeRequest.getAmount());
            page = "redirect:/";
        }
        return page;
    }
}
