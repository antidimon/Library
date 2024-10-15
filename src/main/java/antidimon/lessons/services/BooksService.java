package antidimon.lessons.services;


import antidimon.lessons.dao.BookDAO;
import antidimon.lessons.models.Book;
import antidimon.lessons.models.Person;
import antidimon.lessons.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;
    private final BookDAO bookDAO;

    @Autowired
    public BooksService(BooksRepository booksRepository, BookDAO bookDAO) {
        this.booksRepository = booksRepository;
        this.bookDAO = bookDAO;
    }

    public List<Book> getAllBooks() {
        return booksRepository.findAll();
    }

    public Book show(long id) {
        Optional<Book> foundedBook = booksRepository.findById(id);
        return foundedBook.orElse(null);
    }

    public Person getBookOwner(long id) {
        Optional<Book> foundedBook = booksRepository.findById(id);
        return foundedBook.map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(long id, Book updatedBook) {
        Book bookToBeUpdated = booksRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());

        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(long id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void release(long id) {
        booksRepository.findById(id).ifPresent(
                book -> book.setOwner(null)
        );
    }

    @Transactional
    public void assign(long id, Person selectedPerson) {
        Optional<Book> foundedBook = booksRepository.findById(id);
        foundedBook.ifPresent(book -> book.setOwner(selectedPerson));
    }

    public List<Book> searchByTitle(String query) {
        return booksRepository.findByTitleStartingWithIgnoreCase(query);
    }

    public List<Book> getBooks(List<Integer> bookIds) {
        return bookDAO.getBooks(bookIds);
    }

    public int getOwnerId(long id) {
        Optional<Book> foundedBook = booksRepository.findById(id);
        return foundedBook.get().getOwner().getId();
    }
}
