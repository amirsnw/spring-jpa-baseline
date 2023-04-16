package com.hibernate.model.one.to.one.sharedPK.bi;

import javax.persistence.*;

@Entity
public class Manuscript {

    @Id
    private Long id;

    @Column(name="water-mark")
    private String waterMark;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private Book book;

    public Manuscript() {
    }

    public Manuscript(String waterMark) {
        this.waterMark = waterMark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWaterMark() {
        return waterMark;
    }

    public void setWaterMark(String waterMark) {
        this.waterMark = waterMark;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "Manuscript [" +
                "id=" + id +
                ", waterMark=" + waterMark + "]";
    }
}
