package com.hibernate.model.one.to.many.sharedPK.bi;

import javax.persistence.*;

@Entity
@Table(name="engine")
public class Engine {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "engine_sequence")
    @SequenceGenerator(name = "engine_sequence", sequenceName = "eng_seq", allocationSize = 1)
    @Column(name="id")
    private int id;

    @Column(name="type")
    private String type;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public Engine() {
    }

    public Engine(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Engine [" +
                "id=" + id +
                ", type=" + type + "]";
    }
}
