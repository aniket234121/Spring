# Dependency Injection Java Based Config

## Constructor Injection

### A. Injecting Primitive and String-Based Values through constructor.

```java
package com.example;

public class Employee {

    private final String name;
    private final int age;
    private final double salary;
    private final boolean active;

    public Employee(String name, int age, double salary, boolean active) {
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

```java
package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Employee employee() {

        return new Employee(
                "Aniket",
                24,
                75000,
                true
        );
    }
}
```
Here the @Bean method is responsible for supplying the constructor arguments.
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
### B. Injecting Dependent Object / Contained Object through constructor

```java
package com.example;

public class EmployeeRepository {

    public void save() {
        System.out.println("Employee saved");
    }
}
```
```java
package com.example;

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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EmployeeRepository employeeRepository() {
        return new EmployeeRepository();
    }

    @Bean
    public EmployeeService employeeService(
            EmployeeRepository employeeRepository) {

        return new EmployeeService(employeeRepository);
    }
}
```
### C. Injecting Collection values through constructor
```java
package com.example;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Properties;

public class ApplicationConfig {

    private final List<String> languages;
    private final Set<String> roles;
    private final Map<String, String> paymentProviders;
    private final Properties settings;

    public ApplicationConfig(
            List<String> languages,
            Set<String> roles,
            Map<String, String> paymentProviders,
            Properties settings) {

        this.languages = languages;
        this.roles = roles;
        this.paymentProviders = paymentProviders;
        this.settings = settings;
    }

    public void display() {
        System.out.println("Languages: " + languages);
        System.out.println("Roles: " + roles);
        System.out.println("Payment Providers: " + paymentProviders);
        System.out.println("Settings: " + settings);
    }
}
```
```java
package com.example;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ApplicationConfig applicationConfig() {

        List<String> languages =
                List.of("Java", "English", "Hindi");

        Set<String> roles =
                Set.of("ADMIN", "USER", "MANAGER");

        Map<String, String> paymentProviders =
                Map.of(
                        "CARD", "Stripe",
                        "UPI", "Razorpay",
                        "WALLET", "Paytm"
                );

        Properties settings = new Properties();

        settings.setProperty("environment", "production");
        settings.setProperty("region", "India");
        settings.setProperty("timeout", "30");

        return new ApplicationConfig(
                languages,
                roles,
                paymentProviders,
                settings
        );
    }
}
```

## Setter Injection

### A. Injecting Primitive and String-Based Values through setter.

```java
package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Employee employee() {

        Employee employee = new Employee();

        employee.setName("Aniket");
        employee.setAge(24);
        employee.setSalary(75000);
        employee.setActive(true);

        return employee;
    }
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

public class EmployeeRepository {

    public void save() {
        System.out.println("Employee saved");
    }
}
```

```java
package com.example;

public class EmployeeService {

    private EmployeeRepository repository;

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
package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EmployeeRepository employeeRepository() {
        return new EmployeeRepository();
    }

    @Bean
    public EmployeeService employeeService(
            EmployeeRepository employeeRepository) {

        EmployeeService service = new EmployeeService();

        service.setRepository(employeeRepository);

        return service;
    }
}
```
### C. Injecting Collection values through setter
```java
package com.example;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Properties;

public class ApplicationConfig {

    private List<String> languages;
    private Set<String> roles;
    private Map<String, String> paymentProviders;
    private Properties settings;

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void setPaymentProviders(Map<String, String> paymentProviders) {
        this.paymentProviders = paymentProviders;
    }

    public void setSettings(Properties settings) {
        this.settings = settings;
    }

    public void display() {
        System.out.println("Languages: " + languages);
        System.out.println("Roles: " + roles);
        System.out.println("Payment Providers: " + paymentProviders);
        System.out.println("Settings: " + settings);
    }
}
```
```java
package com.example;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ApplicationConfig applicationConfig() {

        ApplicationConfig config =
                new ApplicationConfig();

        config.setLanguages(
                List.of("Java", "English", "Hindi")
        );

        config.setRoles(
                Set.of("ADMIN", "USER", "MANAGER")
        );

        config.setPaymentProviders(
                Map.of(
                        "CARD", "Stripe",
                        "UPI", "Razorpay",
                        "WALLET", "Paytm"
                )
        );

        Properties settings = new Properties();

        settings.setProperty("environment", "production");
        settings.setProperty("region", "India");
        settings.setProperty("timeout", "30");

        config.setSettings(settings);

        return config;
    }
}
```