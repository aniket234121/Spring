package springCore.DependencyInjection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springCore.Beans.Student;

@Configuration
public class AnnotationsBeanConfigForDI {

    //Constructor config

    @Bean
    public Student studentBean1ForCI()
    {
        return new Student(12,"ramesh",(byte)21);
    }

    //Setter Config

    @Bean
    public Student studentBean1ForSI()
    {
        Student student=new Student();
        student.setId(13);
        student.setName("rajnesh");
        student.setAge((byte)21);
        return student;
    }

}
