package ua.com.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.com.taxi.domain.Car;
import ua.com.taxi.service.CarService;

import java.util.List;

@Controller
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/admin/car-list")
    public String getCarList(Model model) {
        List<Car> cars = carService.findAll();
        model.addAttribute("cars", cars);
        return "car/car-list";
    }
}
