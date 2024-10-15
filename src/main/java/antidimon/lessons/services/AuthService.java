package antidimon.lessons.services;


import antidimon.lessons.dao.PersonDAO;
import antidimon.lessons.models.Person;
import antidimon.lessons.repositories.PeopleRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Transactional(readOnly=true)
public class AuthService {

    private final PeopleRepository peopleRepository;
    private final PersonDAO personDAO;
    private final PasswordEncoder passwordEncoder;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public AuthService(PeopleRepository peopleRepository, PersonDAO personDAO, PasswordEncoder passwordEncoder, PersonDetailsService personDetailsService) {
        this.peopleRepository = peopleRepository;
        this.personDAO = personDAO;
        this.passwordEncoder = passwordEncoder;
        this.personDetailsService = personDetailsService;
    }


    @Transactional
    public void registerPerson(Person person){
        encodePersonPassword(person);
        person.setRole("USER");
        person.setTicket(createNewTicket());
        peopleRepository.save(person);
    }

    public void encodePersonPassword(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
    }

    public String getRoleHomePage(Collection<? extends GrantedAuthority> authorities) {
        return (personDetailsService.isUserAdmin(authorities)) ? "admin/home" : "user/home";
    }


    private int createNewTicket(){
        return personDAO.getMaxTicket() + 1;
    }
}
