package antidimon.lessons.utils;

import antidimon.lessons.models.Person;
import antidimon.lessons.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (peopleService.isUsernameExists(person.getUsername())){
            errors.rejectValue("username", "", "Username already exists");
        }
        if (person.getFullName().split(" ").length != 2){
            errors.rejectValue("fullName", "", "You should insert name and surname right (Name Surname)");
        }
    }
}
