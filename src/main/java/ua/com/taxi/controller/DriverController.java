package ua.com.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.com.taxi.domain.Driver;
import ua.com.taxi.service.DriverService;

import java.util.List;

@Controller
public class DriverController {

    private DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/admin/driver-list")
    public String getAll(Model model) {
        List<Driver> drivers = driverService.findAll();
        model.addAttribute("drivers", drivers);
        return "driver/driver-list";
    }
}
