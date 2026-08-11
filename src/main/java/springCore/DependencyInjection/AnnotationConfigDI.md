# Dependency Injection

## 1. Constructor Injection

### A. Injecting Primitive and String-Based Values through constructor.

Employee.java
```java
package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Employee {

    private final String name;
    private final int age;
    private final double salary;
    private final boolean active;

    public Employee(
            @Value("Aniket") String name,
            @Value("24") int age,
            @Value("75000") double salary,
            @Value("true") boolean active) {

        this.name = name;
        this.age = age;
        this.salary = salary;
        this.active = active;
    }

    public void display() {
        System.out.println("Name   : " + name);
        System.out.println("Age    : " + age);
        System.out.println("Salary : " + salary);
        System.out.println("Active : " + active);
    }
}
```
AppConfig.java
```java
package com.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example")
public class AppConfig {
}
```
### B. Injecting Dependent Object / Contained Object through constructor

```java
package com.example;

import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    public void save() {
        System.out.println("Employee saved");
    }
}
```
```java
package com.example;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public void addEmployee() {
        repository.save();
        System.out.println("Employee service executed");
    }
}
```
```java
package com.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example")
public class AppConfig {
}
```
```java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        EmployeeService service =
                context.getBean(EmployeeService.class);

        service.addEmployee();
    }
}
```
### C. Injecting Collection values through constructor
```java
package com.example;

public interface PaymentProcessor {

    void process();
}
```
```java
package com.example;

import org.springframework.stereotype.Component;

@Component
public class CardProcessor implements PaymentProcessor {

    @Override
    public void process() {
        System.out.println("Processing card payment");
    }
}
```
```java
package com.example;

import org.springframework.stereotype.Component;

@Component
public class UpiProcessor implements PaymentProcessor {

    @Override
    public void process() {
        System.out.println("Processing UPI payment");
    }
}
```
```java
package com.example;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final List<PaymentProcessor> processors;

    public PaymentService(List<PaymentProcessor> processors) {
        this.processors = processors;
    }

    public void processPayments() {

        for (PaymentProcessor processor : processors) {
            processor.process();
        }
    }
}
```
```java
package com.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example")
public class AppConfig {
}
```
## 2. Setter Injection

### A. Injecting Primitive and String-Based Values through setter.
```java
package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Employee {

    private String name;
    private int age;
    private double salary;
    private boolean active;

    @Value("${employee.name}")
    public void setName(String name) {
        this.name = name;
    }

    @Value("${employee.age}")
    public void setAge(int age) {
        this.age = age;
    }

    @Value("${employee.salary}")
    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Value("${employee.active}")
    public void setActive(boolean active) {
        this.active = active;
    }

    public void display() {
        System.out.println("Name   : " + name);
        System.out.println("Age    : " + age);
        System.out.println("Salary : " + salary);
        System.out.println("Active : " + active);
    }
}
```
application.properties
```java
employee.name=Aniket
employee.age=24
employee.salary=75000
employee.active=true
```
AppConfig.java
```java
package com.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.example")
@PropertySource("classpath:application.properties")
public class AppConfig {
}
```
```java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        Employee employee =
                context.getBean(Employee.class);

        employee.display();
    }
}
```

### B. Injecting Dependent Object / Contained Object through setter

```java
package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private EmployeeRepository repository;

    @Autowired
    public void setRepository(EmployeeRepository repository) {
        this.repository = repository;
    }

    public void addEmployee() {
        repository.save();
        System.out.println("Employee service executed");
    }
}
```
```java
@Repository
public class EmployeeRepository {

    public void save() {
        System.out.println("Employee saved");
    }
}
```
### C. Injecting Collection values through setter
```java
package com.example;

public interface PaymentProcessor {

    void process();
}
```
```java
package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private List<PaymentProcessor> processors;

    @Autowired
    public void setProcessors(List<PaymentProcessor> processors) {
        this.processors = processors;
    }

    public void processPayments() {

        for (PaymentProcessor processor : processors) {
            processor.process();
        }
    }
}
```
```java
package com.example;

import org.springframework.stereotype.Component;

@Component
public class CardProcessor implements PaymentProcessor {

    @Override
    public void process() {
        System.out.println("Processing card payment");
    }
}
```
```java
package com.example;

import org.springframework.stereotype.Component;

@Component
public class UpiProcessor implements PaymentProcessor {

    @Override
    public void process() {
        System.out.println("Processing UPI payment");
    }
}
```
```java
package com.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example")
public class AppConfig {
}
```
```java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        PaymentService service =
                context.getBean(PaymentService.class);

        service.processPayments();
    }
}
```