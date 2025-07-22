package springCore.DependencyInjection;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import springCore.Beans.Student;

public class MainDI {
    public static void annotationBased(){
        AnnotationConfigApplicationContext ctx=new AnnotationConfigApplicationContext(springCore.DependencyInjection.AnnotationsBeanConfigForDI.class);
        System.out.println("--------Annotations Based Config-------");

        System.out.println("Constructor Injection");                            //constructor injection
        Student student1=ctx.getBean("studentBean1ForCI",Student.class);
        System.out.println(student1);

        System.out.println("Setter Injection");                            //constructor injection
        Student student2=ctx.getBean("studentBean1ForSI",Student.class);
        System.out.println(student2);

    }
    public static void XMLBased()
    {
        System.out.println("---------XML Based-----------");
        System.out.println("setter injection");
        ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("BeanConfigForDI.xml");
        Student st=ctx.getBean("student3",springCore.Beans.Student.class);
        System.out.println(st);

        System.out.println("constructor injection");
        Student st2=ctx.getBean("student4",springCore.Beans.Student.class);
        System.out.println(st2);

    }
    public static void main(String[] args) {
        annotationBased();
        XMLBased();

    }
}
