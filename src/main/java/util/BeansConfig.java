package util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springCore.Beans.Student;

@Configuration
public class BeansConfig {
    @Bean
    public Student student1(){
        return new Student(2,"sakshi chauhan", (byte) 17);
    }
    @Bean
    public Student student2(){
        return new Student(3,"pragya chauhan", (byte) 15);
    }
}
