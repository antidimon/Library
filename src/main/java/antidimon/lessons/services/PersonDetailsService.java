package antidimon.lessons.services;

import antidimon.lessons.models.Person;
import antidimon.lessons.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> foundedPerson = peopleRepository.findByUsername(username);

        if(foundedPerson.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        Person person = foundedPerson.get();
        return User.builder().username(person.getUsername())
                .password(person.getPassword())
                .roles(unwrapAuthorities(person.getAuthorities()))
                .disabled(!person.isEnabled())
                .build();
    }


    private String unwrapAuthorities(Collection<? extends GrantedAuthority> authorities){
        return authorities.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public boolean isUserAdmin(Collection<? extends GrantedAuthority> authorities){
        return authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

}
