# CrudRepository 

**CrudRepository<T, ID>** is a Spring Data repository interface that provides standard CRUD (Create, Read, Update, Delete) operations for a domain/entity type.

It is part of Spring Data Commons, so it is not specific to JPA.

For Spring Data JPA, it can be used as a repository abstraction without directly writing the implementation.

```java
public interface EmployeeRepository
        extends CrudRepository<Employee, Long> {
}
```
Here:

* Employee → domain/entity type
* Long → type of the entity's identifier (primary key)

## Purpose of CrudRepository

CrudRepository provides a standard abstraction for common persistence operations:

    Create
    Read
    Update
    Delete

It eliminates the need to manually create repository implementations for standard operations.

For example, instead of writing:

```java
public Employee save(Employee employee) {
// EntityManager logic
}
```

you can simply use:

    employeeRepository.save(employee);

## Methods in CRUD Repository

| Method                | Return type   | Purpose                  |
| --------------------- | ------------- | ------------------------ |
| `save(entity)`        | `S`           | Save new/existing entity |
| `saveAll(entities)`   | `Iterable<S>` | Save multiple entities   |
| `findById(id)`        | `Optional<T>` | Find by primary key      |
| `findAll()`           | `Iterable<T>` | Retrieve all             |
| `findAllById(ids)`    | `Iterable<T>` | Retrieve by multiple IDs |
| `existsById(id)`      | `boolean`     | Check existence          |
| `count()`             | `long`        | Count entities           |
| `deleteById(id)`      | `void`        | Delete by ID             |
| `delete(entity)`      | `void`        | Delete supplied entity   |
| `deleteAllById(ids)`  | `void`        | Delete by multiple IDs   |
| `deleteAll(entities)` | `void`        | Delete supplied entities |
| `deleteAll()`         | `void`        | Delete all entities      |

## Example 

### Entity
```java
package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double salary;

    public Employee() {
    }

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}
```
### Repository
```java
package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository
        extends CrudRepository<Employee, Long> {
}
```
### Here we use CommandLineRunner to execute repository methods when the application starts.
```java
package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class CrudDemoApplication implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    public CrudDemoApplication(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CrudDemoApplication.class, args);
    }

    @Override
    public void run(String... args) {

        System.out.println("\n========== CRUD REPOSITORY DEMO ==========\n");

        // =========================================================
        // 1. save()
        // =========================================================

        Employee employee1 =
                new Employee("Rahul", 70000);

        Employee savedEmployee =
                employeeRepository.save(employee1);

        System.out.println("1. save()");
        System.out.println("Saved: " + savedEmployee);

        /*
         * Conceptual SQL:
         *
         * INSERT INTO employees (name, salary)
         * VALUES (?, ?);
         *
         * Example:
         * id = 1
         * name = Rahul
         * salary = 70000
         */


        // =========================================================
        // 2. save() - another employee
        // =========================================================

        Employee employee2 =
                new Employee("Amit", 80000);

        Employee employee2Saved =
                employeeRepository.save(employee2);

        System.out.println("\n2. save()");
        System.out.println("Saved: " + employee2Saved);

        /*
         * Conceptual SQL:
         *
         * INSERT INTO employees (name, salary)
         * VALUES (?, ?);
         *
         * Example:
         * id = 2
         * name = Amit
         * salary = 80000
         */


        // =========================================================
        // 3. saveAll()
        // =========================================================

        Employee employee3 =
                new Employee("Priya", 90000);

        Employee employee4 =
                new Employee("Neha", 60000);

        Iterable<Employee> savedEmployees =
                employeeRepository.saveAll(
                        Arrays.asList(employee3, employee4)
                );

        System.out.println("\n3. saveAll()");
        savedEmployees.forEach(System.out::println);

        /*
         * Conceptual SQL:
         *
         * INSERT INTO employees (name, salary)
         * VALUES (?, ?);
         *
         * INSERT INTO employees (name, salary)
         * VALUES (?, ?);
         *
         * Important:
         * saveAll() does NOT automatically mean one SQL statement.
         */


        // =========================================================
        // 4. findById()
        // =========================================================

        Optional<Employee> employee =
                employeeRepository.findById(1L);

        System.out.println("\n4. findById(1)");

        employee.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Employee not found")
        );

        /*
         * Conceptual SQL:
         *
         * SELECT
         *     e.id,
         *     e.name,
         *     e.salary
         * FROM employees e
         * WHERE e.id = ?;
         *
         * Output:
         * Employee{id=1, name='Rahul', salary=70000.0}
         */


        // =========================================================
        // 5. findAll()
        // =========================================================

        Iterable<Employee> employees =
                employeeRepository.findAll();

        System.out.println("\n5. findAll()");

        employees.forEach(System.out::println);

        /*
         * Conceptual SQL:
         *
         * SELECT
         *     e.id,
         *     e.name,
         *     e.salary
         * FROM employees e;
         *
         * Output:
         *
         * Employee{id=1, name='Rahul', salary=70000.0}
         * Employee{id=2, name='Amit', salary=80000.0}
         * Employee{id=3, name='Priya', salary=90000.0}
         * Employee{id=4, name='Neha', salary=60000.0}
         */


        // =========================================================
        // 6. findAllById()
        // =========================================================

        List<Long> ids =
                Arrays.asList(1L, 3L);

        Iterable<Employee> selectedEmployees =
                employeeRepository.findAllById(ids);

        System.out.println("\n6. findAllById([1, 3])");

        selectedEmployees.forEach(System.out::println);

        /*
         * Conceptual SQL:
         *
         * SELECT
         *     e.id,
         *     e.name,
         *     e.salary
         * FROM employees e
         * WHERE e.id IN (?, ?);
         *
         * Output:
         *
         * Employee{id=1, name='Rahul', salary=70000.0}
         * Employee{id=3, name='Priya', salary=90000.0}
         *
         * Important:
         * Result ordering is not guaranteed to match
         * the order of supplied IDs.
         */


        // =========================================================
        // 7. existsById()
        // =========================================================

        boolean exists =
                employeeRepository.existsById(1L);

        System.out.println("\n7. existsById(1)");
        System.out.println("Exists: " + exists);

        /*
         * Conceptual SQL:
         *
         * SELECT
         *     1
         * FROM employees
         * WHERE id = ?;
         *
         * Output:
         *
         * Exists: true
         */


        // =========================================================
        // 8. count()
        // =========================================================

        long count =
                employeeRepository.count();

        System.out.println("\n8. count()");
        System.out.println("Employee count: " + count);

        /*
         * Conceptual SQL:
         *
         * SELECT COUNT(*)
         * FROM employees;
         *
         * Output:
         *
         * Employee count: 4
         */


        // =========================================================
        // 9. save() - UPDATE existing entity
        // =========================================================

        Employee employeeToUpdate =
                employeeRepository.findById(1L)
                        .orElseThrow();

        employeeToUpdate.setSalary(75000);

        Employee updatedEmployee =
                employeeRepository.save(employeeToUpdate);

        System.out.println("\n9. save() - UPDATE");
        System.out.println("Updated: " + updatedEmployee);

        /*
         * Conceptual SQL:
         *
         * SELECT ...
         * FROM employees
         * WHERE id = ?;
         *
         * UPDATE employees
         * SET name = ?,
         *     salary = ?
         * WHERE id = ?;
         *
         * Important:
         * save() is used for both new and existing entities.
         *
         * For an existing entity, Spring Data/JPA can result
         * in an UPDATE operation.
         */


        // =========================================================
        // 10. deleteById()
        // =========================================================

        employeeRepository.deleteById(4L);

        System.out.println("\n10. deleteById(4)");
        System.out.println("Employee 4 deleted");

        /*
         * Conceptual SQL:
         *
         * DELETE FROM employees
         * WHERE id = ?;
         */


        // =========================================================
        // 11. delete()
        // =========================================================

        Employee employeeToDelete =
                employeeRepository.findById(3L)
                        .orElseThrow();

        employeeRepository.delete(employeeToDelete);

        System.out.println("\n11. delete(employee)");
        System.out.println("Employee 3 deleted");

        /*
         * Conceptual SQL:
         *
         * DELETE FROM employees
         * WHERE id = ?;
         */


        // =========================================================
        // 12. deleteAllById()
        // =========================================================

        employeeRepository.deleteAllById(
                Arrays.asList(1L, 2L)
        );

        System.out.println("\n12. deleteAllById([1, 2])");
        System.out.println("Employees 1 and 2 deleted");

        /*
         * Conceptual SQL:
         *
         * DELETE FROM employees
         * WHERE id = ?;
         *
         * DELETE FROM employees
         * WHERE id = ?;
         *
         * Exact SQL depends on the JPA provider and configuration.
         */


        // =========================================================
        // 13. saveAll() again
        // =========================================================

        Employee employee5 =
                new Employee("Karan", 65000);

        Employee employee6 =
                new Employee("Sneha", 85000);

        employeeRepository.saveAll(
                Arrays.asList(employee5, employee6)
        );

        System.out.println("\n13. saveAll()");

        /*
         * Conceptual SQL:
         *
         * INSERT INTO employees (name, salary)
         * VALUES (?, ?);
         *
         * INSERT INTO employees (name, salary)
         * VALUES (?, ?);
         */


        // =========================================================
        // 14. deleteAll(Iterable)
        // =========================================================

        Iterable<Employee> employeesToDelete =
                employeeRepository.findAll();

        employeeRepository.deleteAll(employeesToDelete);

        System.out.println("\n14. deleteAll(Iterable)");
        System.out.println("All retrieved employees deleted");

        /*
         * Conceptual SQL:
         *
         * DELETE FROM employees
         * WHERE id = ?;
         *
         * DELETE FROM employees
         * WHERE id = ?;
         *
         * ...
         *
         * This deletes the supplied entities,
         * not necessarily through one bulk SQL statement.
         */


        // =========================================================
        // 15. save() after deleteAll
        // =========================================================

        Employee employee7 =
                new Employee("Vikas", 72000);

        Employee employee8 =
                new Employee("Anjali", 95000);

        employeeRepository.saveAll(
                Arrays.asList(employee7, employee8)
        );

        System.out.println("\n15. Added final employees");

        /*
         * Conceptual SQL:
         *
         * INSERT INTO employees (name, salary)
         * VALUES (?, ?);
         *
         * INSERT INTO employees (name, salary)
         * VALUES (?, ?);
         */


        // =========================================================
        // 16. deleteAll()
        // =========================================================

        employeeRepository.deleteAll();

        System.out.println("\n16. deleteAll()");
        System.out.println("All employees deleted");

        /*
         * Conceptual SQL:
         *
         * DELETE FROM employees
         * WHERE id = ?;
         *
         * ...
         *
         * IMPORTANT:
         *
         * CrudRepository.deleteAll() is entity-oriented deletion.
         * It is not the same as a custom bulk JPQL/SQL DELETE query.
         */


        System.out.println("\n========== DEMO COMPLETE ==========\n");
    }
}
```