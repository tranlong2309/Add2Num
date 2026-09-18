package com.add2num.controller;

import com.add2num.service.MyBigNumber;
import com.add2num.service.MyBigNumber.AdditionResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring MVC controller for the Add-Two-Numbers web UI.
 * Thin controller: all calculation logic is delegated to {@link MyBigNumber}.
 */
@Controller
public class AddNumberController {

    private static final Logger log = LoggerFactory.getLogger(AddNumberController.class);

    private final MyBigNumber myBigNumber = new MyBigNumber();

    /**
     * Simple form backing object for model binding + validation.
     */
    public static class AddForm {
        @NotBlank(message = "First number is required")
        @Pattern(regexp = "\\d+", message = "Only digits allowed (no spaces, signs or decimals)")
        private String number1 = "";

        @NotBlank(message = "Second number is required")
        @Pattern(regexp = "\\d+", message = "Only digits allowed (no spaces, signs or decimals)")
        private String number2 = "";

        public String getNumber1() { return number1; }
        public void   setNumber1(String n) { this.number1 = n.trim(); }
        public String getNumber2() { return number2; }
        public void   setNumber2(String n) { this.number2 = n.trim(); }
    }

    /** Keeps an in-memory list of the last 10 calculations (history panel). */
    private final List<AdditionResult> history = new ArrayList<>();

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("form", new AddForm());
        model.addAttribute("history", history);
        return "index";
    }

    @PostMapping("/calculate")
    public String calculate(@ModelAttribute("form") AddForm form,
                            BindingResult bindingResult,
                            Model model) {

        model.addAttribute("history", history);

        if (bindingResult.hasErrors()) {
            return "index";
        }

        String a = form.getNumber1();
        String b = form.getNumber2();

        log.info("Received calculation request: {} + {}", a, b);

        AdditionResult calcResult = myBigNumber.sum(a, b);

        log.info("Calculation complete: {} + {} = {}, steps={}",
                a, b, calcResult.getResult(), calcResult.getSteps().size());

        // Prepend to history (newest first), keep only last 10
        history.add(0, calcResult);
        if (history.size() > 10) history.remove(history.size() - 1);

        model.addAttribute("calcResult", calcResult);
        return "index";
    }
}
