package springCore;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import springCore.Beans.Student;

/**
 * Hello world!
 */
public class IOCandBeanDemo {
    public static void main(String[] args) {

        //Xml based

        ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("BeansConfig.xml");
        Student st1=ctx.getBean("student1", Student.class);
        System.out.println(st1);

        //Annotations based

        AnnotationConfigApplicationContext acax=new AnnotationConfigApplicationContext(util.BeansConfig.class);
        System.out.println(acax.getBean("student2", Student.class));
    }
}
