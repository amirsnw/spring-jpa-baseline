package com.hibernate.model.one.to.one.sharedPK.bi;

import javax.persistence.*;

@Entity
@Table(name="book")
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "book_sequence")
    @SequenceGenerator(name = "book_sequence", sequenceName = "book_seq", allocationSize = 1)
    @Column(name="id")
    private int id;

    @Column(name="title")
    private String title;

    @OneToOne(mappedBy = "book")
    private Manuscript manuscript;

    public Book() {
    }

    public Book(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Manuscript getManuscript() {
        return manuscript;
    }

    public void setManuscript(Manuscript manuscript) {
        this.manuscript = manuscript;
    }

    @Override
    public String toString() {
        return "Book [" +
                "id=" + id +
                ", title=" + title + "]";
    }
}
