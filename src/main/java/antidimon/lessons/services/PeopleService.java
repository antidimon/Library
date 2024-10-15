package antidimon.lessons.services;

import antidimon.lessons.dao.PersonDAO;
import antidimon.lessons.models.Book;
import antidimon.lessons.models.Person;
import antidimon.lessons.repositories.PeopleRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final PersonDAO personDAO;
    private final BooksService booksService;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PersonDAO personDAO, BooksService booksService) {
        this.peopleRepository = peopleRepository;
        this.personDAO = personDAO;
        this.booksService = booksService;
    }
    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    public Person getPerson(String username) {
        Optional<Person> foundedPerson = peopleRepository.findByUsername(username);

        return foundedPerson.orElse(null);
    }
    public Person getPerson(int id){
        Optional<Person> foundedPerson = peopleRepository.findById(id);

        return foundedPerson.orElse(null);
    }
    public boolean isUsernameExists(String username) {
        return peopleRepository.findByUsername(username).isPresent();
    }

    public List<Book> getPersonBooks(String username) {
        Optional<Person> foundedPerson = peopleRepository.findByUsername(username);

        if (foundedPerson.isPresent()){
            Hibernate.initialize(foundedPerson.get().getBooks());
            return foundedPerson.get().getBooks();
        }
        else {
            return Collections.emptyList();
        }
    }

    public List<Person> index() {
        return peopleRepository.findAll();
    }

    public List<Book> getPersonHistory(String username) {
        Person person = this.getPerson(username);
        if (person == null) return Collections.emptyList();

        List<Integer> bookIds = personDAO.getUserHistoryBookIds(person.getId());
        if (bookIds.isEmpty()) return Collections.emptyList();
        return booksService.getBooks(bookIds);
    }

    public void rememberBook(long book_id, int person_id) {
        personDAO.rememberBook(book_id, person_id);
    }

    public void update(int id, Person person) {
        Person personToBeUpdated = peopleRepository.findById(id).get();

        person.setId(id);
        person.setBooks(personToBeUpdated.getBooks());
        person.setFullName(personToBeUpdated.getFullName());
        person.setTicket(personToBeUpdated.getTicket());
        person.setUsername(personToBeUpdated.getUsername());
        person.setPassword(personToBeUpdated.getPassword());
        person.setBlocked(personToBeUpdated.getBlocked());

        peopleRepository.save(person);
    }
    @Transactional
    public void block(int id, boolean block) {
        Person person = peopleRepository.findById(id).get();

        person.setBlocked(block);

        peopleRepository.save(person);
    }

    public boolean isUserBlocked(String username) {
        Person person = this.getPerson(username);
        return !person.isEnabled();
    }
}
