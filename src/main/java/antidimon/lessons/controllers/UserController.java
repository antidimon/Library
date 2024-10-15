package antidimon.lessons.controllers;

import antidimon.lessons.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final PeopleService peopleService;

    @Autowired
    public UserController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/home")
    public String userHome(Model model, Authentication authentication){

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (peopleService.isUserBlocked(userDetails.getUsername())){
            return "user/block";
        }
        model.addAttribute("person", peopleService.getPerson(userDetails.getUsername()));
        return "user/home";
    }

    @GetMapping("/history")
    public String userHistory(Model model, Authentication authentication){

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (peopleService.isUserBlocked(userDetails.getUsername())){
            return "user/block";
        }
        model.addAttribute("person", peopleService.getPerson(userDetails.getUsername()));
        model.addAttribute("books", peopleService.getPersonHistory(userDetails.getUsername()));

        return "user/history";

    }

    @GetMapping("/books")
    public String userBooks(Model model, Authentication authentication){

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (peopleService.isUserBlocked(userDetails.getUsername())){
            return "user/block";
        }
        model.addAttribute("person", peopleService.getPerson(userDetails.getUsername()));
        model.addAttribute("books", peopleService.getPersonBooks(userDetails.getUsername()));

        return "user/books";
    }
}
