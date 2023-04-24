package com.hibernate.dao.mapping.one.to.many.sharedPK.bi;

import com.hibernate.model.one.to.many.sharedPK.bi.Student;
import com.hibernate.model.one.to.many.sharedPK.bi.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test One To Many Bidirectional Mapping by Shared Primary Key")
public class MappingOneToManySharedPkBi {

	void createAddStudents() {

		// create session factory
		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Teacher.class)
				.addAnnotatedClass(Student.class)
				.buildSessionFactory();

		// create session
		Session session = factory.getCurrentSession();

		try {
			// start a transaction
			session.beginTransaction();

			// get the teacher from db
			int theId = 67;
			Teacher tempTeacher = session.get(Teacher.class, theId);

			// create some students
			Student tempStudent1 = new Student("Nataly Vaughn", 44);
			Student tempStudent2 = new Student("Marc Boyd", 30);

			// add students to teacher
			tempTeacher.addStudent(tempStudent1);
			tempTeacher.addStudent(tempStudent2);

			// save the students
			session.save(tempStudent1);
			session.save(tempStudent2);

			// commit transaction
			session.getTransaction().commit();

			System.out.println("Done!");
		}
		finally {

			// add clean up code
			session.close();

			factory.close();
		}
	}

	void createTeacher() {

		// create session factory
		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Teacher.class)
				.addAnnotatedClass(Student.class)
				.buildSessionFactory();

		// create session
		Session session = factory.getCurrentSession();

		try {

			// create the objects
			Teacher tempTeacher =
					new Teacher("Rudy Lowe");

			// start a transaction
			session.beginTransaction();

			// save the teacher
			System.out.println("Saving teacher: " + tempTeacher);
			session.save(tempTeacher);

			// commit transaction
			session.getTransaction().commit();

			System.out.println("Done!");
		}
		finally {
			factory.close();
		}
	}

	void deleteStudent() {

		// create session factory
		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Teacher.class)
				.addAnnotatedClass(Student.class)
				.buildSessionFactory();

		// create session
		Session session = factory.getCurrentSession();

		try {

			// start a transaction
			session.beginTransaction();

			// get a student
			int theId = 10;
			Student tempStudent = session.get(Student.class, theId);

			// delete student
			System.out.println("Deleting student: " + tempStudent);

			session.delete(tempStudent);

			// commit transaction
			session.getTransaction().commit();

			System.out.println("Done!");
		}
		finally {

			// add clean up code
			session.close();

			factory.close();
		}
	}

	void deleteTeacher() {

		// create session factory
		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Teacher.class)
				.buildSessionFactory();

		// create session
		Session session = factory.getCurrentSession();

		try {

			// start a transaction
			session.beginTransaction();

			// get teacher by primary key / id
			int theId = 1;
			Teacher tempTeacher =
					session.get(Teacher.class, theId);

			System.out.println("Found teacher: " + tempTeacher);

			// delete the teacher
			if (tempTeacher != null) {

				System.out.println("Deleting: " + tempTeacher);

				session.delete(tempTeacher);
			}

			// commit transaction
			session.getTransaction().commit();

			System.out.println("Done!");
		}
		finally {
			factory.close();
		}
	}

	void getTeacherStudents() {

		// create session factory
		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Teacher.class)
				.addAnnotatedClass(Student.class)
				.buildSessionFactory();

		// create session
		Session session = factory.getCurrentSession();

		try {

			// start a transaction
			session.beginTransaction();

			// get the teacher from db
			int theId = 67;
			Teacher tempTeacher = session.get(Teacher.class, theId);

			System.out.println("Teacher: " + tempTeacher);

			// get students for the teacher
			System.out.println("Students: " + tempTeacher.getStudents());

			// commit transaction
			session.getTransaction().commit();

			System.out.println("Done!");
		}
		finally {

			// add clean up code
			session.close();

			factory.close();
		}
	}
}



