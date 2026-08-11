# Dependency Injection XML Based

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
applicationContext.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="employee"
          class="com.example.Employee">

        <constructor-arg value="Aniket"/>
        <constructor-arg value="24"/>
        <constructor-arg value="75000"/>
        <constructor-arg value="true"/>

    </bean>

</beans>
```
```java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

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

```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="employeeRepository"
          class="com.example.EmployeeRepository"/>

    <bean id="employeeService"
          class="com.example.EmployeeService">

        <constructor-arg ref="employeeRepository"/>  //important

    </bean>

</beans>
```
```java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        EmployeeService service =
                context.getBean(EmployeeService.class);

        service.addEmployee();
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

```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="applicationConfig"
          class="com.example.ApplicationConfig">

        <!-- List -->
        <constructor-arg>
            <list>
                <value>Java</value>
                <value>English</value>
                <value>Hindi</value>
            </list>
        </constructor-arg>

        <!-- Set -->
        <constructor-arg>
            <set>
                <value>ADMIN</value>
                <value>USER</value>
                <value>MANAGER</value>
            </set>
        </constructor-arg>

        <!-- Map -->
        <constructor-arg>
            <map>
                <entry key="CARD" value="Stripe"/>
                <entry key="UPI" value="Razorpay"/>
                <entry key="WALLET" value="Paytm"/>
            </map>
        </constructor-arg>

        <!-- Properties -->
        <constructor-arg>
            <props>
                <prop key="environment">production</prop>
                <prop key="region">India</prop>
                <prop key="timeout">30</prop>
            </props>
        </constructor-arg>

    </bean>

</beans>    
```
```java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        ApplicationConfig config =
                context.getBean("applicationConfig", ApplicationConfig.class);

        config.display();
    }
}
```

## Setter Injection

### A. injecting Primitive and String-Based Values through setter.

```java
package com.example;

public class Employee {

    private String name;
    private int age;
    private double salary;
    private boolean active;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

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
applicationContext.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="employee"
          class="com.example.Employee">

        <property name="name" value="Aniket"/>
        <property name="age" value="24"/>
        <property name="salary" value="75000"/>
        <property name="active" value="true"/>

    </bean>

</beans>
```
```java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

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
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="employeeRepository"
          class="com.example.EmployeeRepository"/>

    <bean id="employeeService"
          class="com.example.EmployeeService">

        <property name="repository"
                  ref="employeeRepository"/>

    </bean>

</beans>
```
```java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        EmployeeService service =
                context.getBean(EmployeeService.class);

        service.addEmployee();
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
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="applicationConfig"
          class="com.example.ApplicationConfig">

        <!-- List -->
        <property name="languages">
            <list>
                <value>Java</value>
                <value>English</value>
                <value>Hindi</value>
            </list>
        </property>

        <!-- Set -->
        <property name="roles">
            <set>
                <value>ADMIN</value>
                <value>USER</value>
                <value>MANAGER</value>
            </set>
        </property>

        <!-- Map -->
        <property name="paymentProviders">
            <map>
                <entry key="CARD" value="Stripe"/>
                <entry key="UPI" value="Razorpay"/>
                <entry key="WALLET" value="Paytm"/>
            </map>
        </property>

        <!-- Properties -->
        <property name="settings">
            <props>
                <prop key="environment">production</prop>
                <prop key="region">India</prop>
                <prop key="timeout">30</prop>
            </props>
        </property>

    </bean>

</beans>
```
```java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        ApplicationConfig config =
                context.getBean("applicationConfig", ApplicationConfig.class);

        config.display();
    }
}
```