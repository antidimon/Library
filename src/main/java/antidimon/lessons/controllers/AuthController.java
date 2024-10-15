package antidimon.lessons.controllers;

import antidimon.lessons.models.Person;
import antidimon.lessons.services.AuthService;
import antidimon.lessons.utils.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AuthService authService;
    private final PersonValidator personValidator;

    @Autowired
    public AuthController(AuthService authService, PersonValidator personValidator) {
        this.authService = authService;
        this.personValidator = personValidator;
    }
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false, name = "error") String exception, Model model, Authentication authentication){
        if (authentication != null){
            return "redirect:" + authService.getRoleHomePage(authentication.getAuthorities());
        }
        if (exception != null){
            model.addAttribute("hasError", true);
            model.addAttribute("errorName", exception);
        }
        return "authentication/login";
    }

    @GetMapping("/register")
    public String registrationPage(Model model, Authentication authentication){
        if (authentication != null){
            return "redirect:" + authService.getRoleHomePage(authentication.getAuthorities());
        }
        model.addAttribute("person", new Person());
        return "authentication/register";
    }
    @PostMapping("/register")
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) return "authentication/register";

        authService.registerPerson(person);

        return "redirect:/login";
    }
}
