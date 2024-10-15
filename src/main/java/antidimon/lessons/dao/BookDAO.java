package antidimon.lessons.dao;

import antidimon.lessons.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional(readOnly = true)
    public List<Book> getBooks(List<Integer> ids){

        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));

        return jdbcTemplate.query(
                String.format("SELECT DISTINCT title, author, year FROM book WHERE id IN (%s)", inSql),
                ids.toArray(),
                (rs, rowNum) -> new Book(rs.getString("title"), rs.getString("author"),
                        rs.getInt("year")));
    }
}
