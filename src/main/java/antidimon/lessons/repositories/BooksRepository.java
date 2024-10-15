package antidimon.lessons.repositories;

import antidimon.lessons.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleStartingWithIgnoreCase(String title);

}
