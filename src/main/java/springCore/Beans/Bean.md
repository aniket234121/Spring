
# How Does a Class Become a Spring Bean?

There are several ways.

Component scanning

    @Component
    public class PaymentService {
    }

or specialized stereotypes:

    @Service
    public class PaymentService {
    }
    @Repository
    public class PaymentRepository {
    }

Spring discovers these through component scanning.

Java configuration
```java
@Configuration
public class AppConfig {

    @Bean
    public PaymentService paymentService() {
        return new PaymentService();
    }
}
```
XML configuration
```xml
<bean id="paymentService"
class="com.example.PaymentService"/>
```

![img.png](img.png)

#### solution: 

    Vehicle vh=context.getBean("vehicle1",Vehicle.class)

### Naming Bean , description and aliases
![img_1.png](img_1.png)

## Bean Lifecycle

```java
1. Spring reads BeanDefinition
          ↓
2. Bean Instantiation
          ↓
3. Dependency Injection / Property Population
          ↓
4. Aware callbacks
          ↓
5. BeanPostProcessor
   postProcessBeforeInitialization()
          ↓
6. Initialization callbacks
          ↓
   @PostConstruct
          ↓
   InitializingBean.afterPropertiesSet()
          ↓
   custom init-method
          ↓
7. BeanPostProcessor
   postProcessAfterInitialization()
          ↓
8. Bean is ready
          ↓
9. Application uses Bean
          ↓
10. Spring Container shuts down
          ↓
11. Destruction callbacks
          ↓
    @PreDestroy
          ↓
    DisposableBean.destroy()
          ↓
    custom destroy-method
```
```java
package com.example.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
class StudentService
        implements BeanNameAware, InitializingBean, DisposableBean {

    public StudentService() {
        System.out.println("1. Constructor: Bean object created");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("2. BeanNameAware: Bean name is " + name);
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("3. @PostConstruct: Initialization logic");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("4. InitializingBean: afterPropertiesSet()");
    }

    public void useService() {
        System.out.println("5. Bean is ready and being used");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("6. @PreDestroy: Cleanup logic");
    }

    @Override
    public void destroy() {
        System.out.println("7. DisposableBean: destroy()");
    }
}

public class BeanLifecycleApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        "com.example.lifecycle"
                );

        StudentService studentService =
                context.getBean(StudentService.class);

        studentService.useService();

        context.close();
    }
}
```
Output:
```java
1. Constructor: Bean object created
2. BeanNameAware: Bean name is studentService
3. @PostConstruct: Initialization logic
4. InitializingBean: afterPropertiesSet()
5. Bean is ready and being used
6. @PreDestroy: Cleanup logic
7. DisposableBean: destroy()
```
### BeanPostProcessor
BeanPostProcessor allows Spring to process a bean before and after initialization.

It has two important methods:

    postProcessBeforeInitialization()
    postProcessAfterInitialization()

### Why is this important?

Spring itself uses post-processing extensively for framework features.

You don't normally write custom BeanPostProcessors in everyday application development, but you should understand their role in the lifecycle.
```java
@Component
public class MyBeanPostProcessor
        implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(
            Object bean,
            String beanName) {

        System.out.println(
                "Before initialization: " + beanName);

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(
            Object bean,
            String beanName) {

        System.out.println(
                "After initialization: " + beanName);

        return bean;
    }
}
```
## Bean Scope
Bean scope determines how many objects of a bean are created and how long those objects are associated with the Spring container.

By default, Spring beans use Singleton scope.

### Bean Scope Types
| Scope         |              Number of Instances | Typical Usage                                    |
| ------------- | -------------------------------: | ------------------------------------------------ |
| `singleton`   |         One per Spring container | Default; most services, repositories, components |
| `prototype`   | New instance each time requested | Stateful objects requiring independent instances |
| `request`     |             One per HTTP request | Web applications                                 |
| `session`     |             One per HTTP session | Web applications                                 |
| `application` |         One per `ServletContext` | Web application-wide state                       |
| `websocket`   |        One per WebSocket session | WebSocket applications                           |

### 1. Singleton 
Singleton Scope

singleton is the default Spring bean scope.

Spring creates one bean instance per IoC container and returns that same instance whenever the bean is requested.

```java
@Component
@Scope("singleton")
public class PaymentService {
}
```
Since singleton is the default, this is normally equivalent to:

```java
@Component
public class PaymentService {
}
```
Example:
```java
PaymentService service1 = context.getBean(PaymentService.class);
PaymentService service2 = context.getBean(PaymentService.class);

System.out.println(service1 == service2);

Output:
true
```
### 2. Prototype Scope

With prototype scope, Spring creates a new bean instance each time the bean is requested from the container.

    @Component
    @Scope("prototype")
    public class PaymentProcessor {
    }

Example:

```java
PaymentProcessor processor1 =
context.getBean(PaymentProcessor.class);

PaymentProcessor processor2 =
context.getBean(PaymentProcessor.class);

System.out.println(processor1 == processor2);

Output:

false
```

### 6. Web-Aware Scopes

These scopes are available when using a web-aware Spring ApplicationContext.

#### 6.1 Request Scope

Creates one bean instance for each HTTP request.

    @Component
    @Scope("request")
    public class RequestData {
    }

Conceptually:

HTTP Request 1 → RequestData instance A

HTTP Request 2 → RequestData instance B

HTTP Request 3 → RequestData instance C

Useful when data should exist only for the duration of a particular request.

#### 6.2 Session Scope

Creates one bean instance for each HTTP session.

    @Component
    @Scope("session")
    public class UserSessionData {
    }

Conceptually:

User Session A → Instance A

User Session B → Instance B

Useful for state associated with a user's session.

#### 6.3 Application Scope

Creates one bean instance per ServletContext.

    @Component
    @Scope("application")
    public class ApplicationData {
    }

The instance is shared across the web application within that servlet context.

#### 6.4 WebSocket Scope

Creates one bean instance per WebSocket session.

    @Component
    @Scope("websocket")
    public class WebSocketData {
    }

The instance exists for the lifetime of the WebSocket session.

## @Scope Annotation

Bean scope can be specified using @Scope.

    @Component
    @Scope("prototype")
    public class Order {
    }

Common values:

* singleton
* prototype
* request
* session
* application
* websocket

You can also use Spring's specialized annotations for web scopes where appropriate.

### 8. XML Configuration

Bean scope can also be configured using XML.

```xml
Singleton
<bean id="paymentService"
class="com.example.PaymentService"
scope="singleton"/>

Prototype
<bean id="paymentProcessor"
class="com.example.PaymentProcessor"
scope="prototype"/>

```
Modern Spring applications generally prefer annotation-based configuration over XML.