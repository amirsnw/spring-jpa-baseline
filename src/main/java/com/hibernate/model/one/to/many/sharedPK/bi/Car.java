package com.hibernate.model.one.to.many.sharedPK.bi;

import javax.persistence.*;

@Entity
@Table(name="car")
public class Car {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "car_sequence")
    @SequenceGenerator(name = "car_sequence", sequenceName = "car_seq", allocationSize = 1)
    @Column(name="car_id")
    private int carId;

    @Column(name="model")
    private String model;

    @OneToOne(mappedBy = "car")
    private Engine engine;

    public Car() {
    }

    public Car(String model) {
        this.model = model;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return "Car [" +
                "carId=" + carId +
                ", model=" + model + "]";
    }
}
