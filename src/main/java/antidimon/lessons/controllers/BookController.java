package antidimon.lessons.controllers;

import antidimon.lessons.models.Book;
import antidimon.lessons.models.Person;
import antidimon.lessons.services.BooksService;
import antidimon.lessons.services.PeopleService;
import antidimon.lessons.services.PersonDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/books")
public class BookController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService, PersonDetailsService personDetailsService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.personDetailsService = personDetailsService;
    }


    @GetMapping()
    public String showAllBooks(Model model, Authentication authentication){

        model.addAttribute("books", booksService.getAllBooks());
        this.setModelAuthProperties(model, authentication);
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model, Authentication authentication, @ModelAttribute("person") Person person) {

        model.addAttribute("book", booksService.show(id));

        Person bookOwner = booksService.getBookOwner(id);
        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", peopleService.index());

        this.setModelAuthProperties(model, authentication);

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        booksService.save(book);
        return "redirect:/books/"+book.getId();
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("book", booksService.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") long id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books/"+id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") long id) {
        peopleService.rememberBook(id, booksService.getOwnerId(id));
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") long id, @ModelAttribute("person") Person selectedPerson) {
        booksService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search(){
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(Authentication authentication, Model model, @RequestParam("query") String query){
        model.addAttribute("books", booksService.searchByTitle(query));
        this.setModelAuthProperties(model, authentication);
        return "books/search";
    }



    private void setModelAuthProperties(Model model, Authentication authentication) {
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("logged", true);
            if (personDetailsService.isUserAdmin(userDetails.getAuthorities())) {
                model.addAttribute("admin", true);
            }
            model.addAttribute("username", userDetails.getUsername());
        }
    }
}
