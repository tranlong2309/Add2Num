package com.add2num.web.controller;

import com.add2num.core.MyBigNumber;
import com.add2num.core.MyBigNumber.AdditionResult;
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
 * Thin Spring MVC controller.
 * All calculation logic is delegated to {@link MyBigNumber} from add2num-core.jar (Task 1).
 */
@Controller
public class AddNumberController {

    private static final Logger log = LoggerFactory.getLogger(AddNumberController.class);

    /** Reusing the core library from Task 1 (add2num-core.jar). */
    private final MyBigNumber myBigNumber = new MyBigNumber();

    /** In-memory history of the last 10 calculations. */
    private final List<AdditionResult> history = new ArrayList<>();

    // ── Form backing object ─────────────────────────────────────────────────

    public static class AddForm {
        @NotBlank(message = "First number is required")
        @Pattern(regexp = "\\d+", message = "Digits only (0–9), no spaces or signs")
        private String number1 = "";

        @NotBlank(message = "Second number is required")
        @Pattern(regexp = "\\d+", message = "Digits only (0–9), no spaces or signs")
        private String number2 = "";

        public String getNumber1()          { return number1; }
        public void   setNumber1(String n)  { this.number1 = n.trim(); }
        public String getNumber2()          { return number2; }
        public void   setNumber2(String n)  { this.number2 = n.trim(); }
    }

    // ── Routes ──────────────────────────────────────────────────────────────

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

        log.info("Calculation request: {} + {}", a, b);

        // ✅ Call sumWithSteps() from add2num-core library to get animation steps
        AdditionResult result = myBigNumber.sumWithSteps(a, b);

        log.info("Result: {} + {} = {} ({} steps)", a, b, result.getResult(), result.getSteps().size());

        // Prepend to history (newest first), keep last 10
        history.add(0, result);
        if (history.size() > 10) history.remove(history.size() - 1);

        model.addAttribute("calcResult", result);
        return "index";
    }
}
