package antidimon.lessons.controllers;

import antidimon.lessons.models.Person;
import antidimon.lessons.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PeopleService peopleService;

    @Autowired
    public AdminController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/home")
    public String adminHome(Model model, Authentication authentication){

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("person", peopleService.getPerson(userDetails.getUsername()));
        return "admin/home";
    }

    @GetMapping("/users")
    public String users(Model model, Authentication authentication){

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("person", peopleService.getPerson(userDetails.getUsername()));
        model.addAttribute("people", peopleService.index());

        return "admin/users";
    }

    @GetMapping("/users/{id}")
    public String editUser(Model model, @PathVariable("id") int id, Authentication authentication){

        model.addAttribute("person", peopleService.getPerson(id));

        return "admin/edit_user";
    }

    @PatchMapping("/users/{id}")
    public String editUser(Model model, @PathVariable("id") int id, @ModelAttribute("person") Person person){

        peopleService.update(id, person);

        return "redirect:/admin/users/"+id;
    }

    @PatchMapping("/users/{id}/block")
    public String blockUser(@PathVariable("id") int id){
        peopleService.block(id, true);

        return "redirect:/admin/users/"+id;
    }

    @PatchMapping("/users/{id}/unblock")
    public String unblockUser(@PathVariable("id") int id){
        peopleService.block(id, false);

        return "redirect:/admin/users/"+id;
    }

}
