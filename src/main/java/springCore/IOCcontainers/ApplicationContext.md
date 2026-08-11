# Application Context
The ApplicationContext is the advanced, feature-rich version of the Spring IoC container. While BeanFactory provides the basic foundation for **Dependency Injection, ApplicationContext** extends it and adds enterprise-specific features needed for real-world applications.

By default, ApplicationContext eagerly loads all Singleton beans. This means it creates and configures your objects the moment the application starts, rather than waiting for you to request them. It also adds built-in support for:

* Event Publishing: Allowing beans to communicate via events.

* AOP Integration: Easy integration with Spring's Aspect-Oriented Programming (for security, logging, etc.).

* Message Resource Handling (i18n): Resolving text messages for internationalization.

## ApplicationContext implementations

| Class                                  | Usage                                      |
|----------------------------------------|--------------------------------------------|
| **AnnotationConfigApplicationContext** | Java/Annotation-based config (modern apps) |
| **ClassPathXmlApplicationContext**     | Reads from XML file inside project         |
| **FileSystemXmlApplicationContext**    | Reads XML config from file system          |
| **WebApplicationContext**              | Used in web applications (Spring MVC)      |

The Spring container is responsible for instantiating, configuring and assembling
objects known as beans, as well as managing their life cycles.

## 1. AnnotationConfigApplicationContext (The Modern Standard)
This is the implementation you use when your application is configured using Java annotations (like **@Configuration, @Bean, and @Component**). This is the standard for modern Spring applications.

Configuration Class:
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Car myCar() {
        return new Car();
    }
}
```
```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        // Load the context using the Java configuration class
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Car car = context.getBean(Car.class);
        car.drive();
    }
}
```
## 2. ClassPathXmlApplicationContext (The Standard XML Way)
This is used when your configuration is written in an XML file, and that XML file is located inside your project's classpath (for example, inside the src/main/resources folder).

XML File (src/main/resources/applicationContext.xml):

```xml
<beans ...>
<bean id="myCar" class="com.example.Car" />
</beans>
```
```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        // Load the context from an XML file found in the classpath
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        Car car = (Car) context.getBean("myCar");
        car.drive();
    }
}
```
## 3. FileSystemXmlApplicationContext (The External XML Way)
This is similar to the classpath version, but it allows you to load the XML configuration file from a specific absolute path on your machine's file system, outside of the compiled classpath.

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        // Load the context from an exact path on your hard drive
        ApplicationContext context = new FileSystemXmlApplicationContext("C:/projects/config/applicationContext.xml");
        // On Mac/Linux, it would look like: "/Users/name/config/applicationContext.xml"
        
        Car car = (Car) context.getBean("myCar");
        car.drive();
    }
}
```
