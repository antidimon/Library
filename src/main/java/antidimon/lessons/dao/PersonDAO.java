package antidimon.lessons.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public int getMaxTicket(){
        Integer value = jdbcTemplate.queryForObject("SELECT MAX(ticket) FROM PERSON", Integer.class);
        return (value == null) ? 1 : value;
    }

    @Transactional(readOnly = true)
    public List<Integer> getUserHistoryBookIds(int id) {
        return jdbcTemplate.queryForList("SELECT book_id FROM history WHERE person_id = ?", Integer.class, id);
    }

    public void rememberBook(long bookId, int person_id) {
        jdbcTemplate.update("INSERT INTO history (person_id, book_id) VALUES (?, ?)", person_id, bookId);
    }
}