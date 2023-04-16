package com.hibernate.model.one.to.one.sharedPK.bi;

import javax.persistence.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "book_seq")
    private Long id;

    @Column(name="title")
    private String title;

    @OneToOne(mappedBy = "book")
    private Manuscript manuscript;

    public Book() {
    }

    public Book(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
