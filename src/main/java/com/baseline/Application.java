package com.baseline;

import com.baseline.dao.mapping.StudentService;
import com.baseline.model.StudentEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
@EnableJpaRepositories("com.baseline")
public class Application {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        System.out.println("****************************************************");

        StudentService service = context.getBean(StudentService.class);

        StudentEntity student = new StudentEntity("Amir", "khalighi", "amirsnw@gmail.com");
        service.save(student);
        List<StudentEntity> students = service.getAll();

        System.out.println(students);

        System.out.println("****************************************************");
    }
}
