package com.hibernate.model.one.to.many.sharedPK.bi;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "teacher_sequence")
    @SequenceGenerator(name = "teacher_sequence", sequenceName = "tch_seq", allocationSize = 1)
    @Column(name="id")
    private int teacherId;

    @Column(name="full_name")
    private String fullName;

    // Primary-key of parent is foreign-key of child
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacher")
    private List<Student> students;

    public Teacher() {
    }

    public Teacher(int teacherId) {
        this.teacherId = teacherId;
    }

    public Teacher(String fullName) {
        this.fullName = fullName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Teacher [" +
                "teacherId=" + teacherId +
                ", fullName=" + fullName + "]";
    }

    // add convenience methods for bi-directional relationship

    public void addStudent(Student student) {

        if (student == null) {
            students = new ArrayList<>();
        }

        students.add(student);

        student.setTeacher(this);
    }
}
