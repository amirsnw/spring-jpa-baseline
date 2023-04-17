package com.hibernate.model.one.to.one.sharedPK.bi;

import javax.persistence.*;

@Entity
@Table(name="manuscript")
public class Manuscript {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "manuscript_sequence")
    @SequenceGenerator(name = "manuscript_sequence", sequenceName = "manu_seq", allocationSize = 1)
    @Column(name="man_id")
    private int id;

    @Column(name="water_mark")
    private String waterMark;

    @OneToOne
    @JoinColumn(name = "man_id")
    @MapsId
    private Book book;

    public Manuscript() {
    }

    public Manuscript(String waterMark) {
        this.waterMark = waterMark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return "Engine [" +
                "id=" + id +
                ", waterMark=" + waterMark + "]";
    }
}
