package me.Dimashi.Exchange_Rate.controller;

import lombok.AllArgsConstructor;
import me.Dimashi.Exchange_Rate.model.MyUser;
import me.Dimashi.Exchange_Rate.services.ApplicationService;
import me.Dimashi.Exchange_Rate.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
//@RestController
@RequestMapping("/api/v1/exchange_rates")
@AllArgsConstructor
@EnableMethodSecurity

public class ExchangeRateController {

    private ApplicationService service;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping
    public String getExchangeRates(Model model) {
        try {
            Map<String, Double> rates = exchangeRateService.getExchangeRates();
            model.addAttribute("rates", rates);
            model.addAttribute("conversionForm", new ConversionForm());
            model.addAttribute("currencies", rates.keySet());
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching exchange rates: " + e.getMessage());
        }
        return "exchange-rates";
    }

    @GetMapping("/convert")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public Map<String, Object> convertCurrency(
            @RequestParam double amount,
            @RequestParam String from,
            @RequestParam String to) {
        try {
            double convertedAmount = exchangeRateService.convert(amount, from, to);
            return Map.of("success", true, "convertedAmount", convertedAmount);
        } catch (Exception e) {
            return Map.of("success", false, "message", "Error during conversion: " + e.getMessage());
        }
    }

    @PostMapping("/convert")
    public String handleCurrencyConversion(@ModelAttribute("conversionForm") ConversionForm conversionForm, Model model) {

        try {
            double amount = conversionForm.getAmount();
            String from = conversionForm.getFrom();
            String to = conversionForm.getTo();

            double convertedAmount = exchangeRateService.convert(amount, from, to);
            model.addAttribute("result", "Converted amount: " + convertedAmount);
        } catch (Exception e) {
            model.addAttribute("error", "Error during conversion: " + e.getMessage());
        }

        return "exchange-rates";
    }

    @PostMapping("/add_user")
    @ResponseBody
    public String addUser(@RequestBody MyUser user) {
        service.addUser(user);
        return "User is Saved";
    }
}
