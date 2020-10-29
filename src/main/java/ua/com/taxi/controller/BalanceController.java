package ua.com.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.taxi.domain.dto.user.RechargeBalance;
import ua.com.taxi.service.UserService;

import java.security.Principal;
import java.util.Locale;

@Controller
public class BalanceController {

    private MessageSource messageSource;

    private UserService userService;

    @Autowired
    public BalanceController(MessageSource messageSource, UserService userService) {
        this.messageSource = messageSource;
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
                                    BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes, Locale locale) {

        String page = "";
        if (bindingResult.hasErrors()) {
            page = "/user/balance-recharge";
        } else {
            userService.rechargeBalance(principal.getName(), rechargeRequest.getAmount());

            String message = messageSource.getMessage("balance-recharge.success-message", new Object[0], locale);
            redirectAttributes.addFlashAttribute("successMessage", message);
            page = String.format("redirect:/user/balance-recharge?amount=%s", rechargeRequest.getAmount());
        }
        return page;
    }
}
