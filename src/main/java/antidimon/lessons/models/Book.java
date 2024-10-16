package antidimon.lessons.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "Название книги не должно быть пустым")
    @Size(min = 2, max = 100, message = "Название книги должно быть от 2 до 100 символов длиной")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Автор не должен быть пустым")
    @Size(min = 2, max = 100, message = "Имя автора должно быть от 2 до 100 символов длиной")
    @Column(name = "author")
    private String author;

    @Min(value = 1500, message = "Год должен быть больше, чем 1500")
    @Column(name = "year")
    private int year;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    public Book(){

    }

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public String getTitle() {return title;
    }
    public void setTitle (String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor (String author) {
        this.author = author;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {this.year = year;}
    public Person getOwner() {return owner;}
    public void setOwner(Person owner) {
        this.owner = owner;
    }

}
