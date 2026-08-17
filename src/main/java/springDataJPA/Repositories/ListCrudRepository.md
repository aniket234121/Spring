# ListCrudRepository
ListCrudRepository is a Spring Data repository interface that provides CRUD operations like CrudRepository, but its collection-returning methods return a List instead of Iterable.

```java
public interface ListCrudRepository<T, ID>
extends CrudRepository<T, ID> {
}
```

## ListCrudRepository Methods
| Method                                      | Parameters                       | Return Type   | Purpose                                                            |
| ------------------------------------------- | -------------------------------- | ------------- | ------------------------------------------------------------------ |
| `save(S entity)`                            | `S entity`                       | `<S> S`       | Saves a new entity or updates an existing entity                   |
| `saveAll(Iterable<S> entities)`             | `Iterable<S> entities`           | **`List<S>`** | Saves multiple entities and returns the saved entities as a `List` |
| `findById(ID id)`                           | `ID id`                          | `Optional<T>` | Finds an entity by its primary key                                 |
| `existsById(ID id)`                         | `ID id`                          | `boolean`     | Checks whether an entity with the given ID exists                  |
| `findAll()`                                 | None                             | **`List<T>`** | Retrieves all entities                                             |
| `findAllById(Iterable<ID> ids)`             | `Iterable<ID> ids`               | **`List<T>`** | Retrieves entities matching the given IDs                          |
| `count()`                                   | None                             | `long`        | Returns the total number of entities                               |
| `deleteById(ID id)`                         | `ID id`                          | `void`        | Deletes an entity by its ID                                        |
| `delete(T entity)`                          | `T entity`                       | `void`        | Deletes the specified entity                                       |
| `deleteAllById(Iterable<? extends ID> ids)` | `Iterable<? extends ID> ids`     | `void`        | Deletes entities corresponding to the given IDs                    |
| `deleteAll(Iterable<? extends T> entities)` | `Iterable<? extends T> entities` | `void`        | Deletes the specified collection of entities                       |
| `deleteAll()`                               | None                             | `void`        | Deletes all entities                                               |

## Example

```java
package com.example.employee;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String department;
    private double salary;

    public Employee() {
    }

    public Employee(String name, String department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }
}
```
```java
package com.example.employee;

import org.springframework.data.repository.ListCrudRepository;

public interface EmployeeRepository
        extends ListCrudRepository<Employee, Long> {
}
```
```java
package com.example.employee;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee createEmployee() {

        Employee employee =
                new Employee("Aniket", "IT", 75000);

        return repository.save(employee);
    }

    public List<Employee> getAllEmployees() {

        return repository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {

        return repository.findById(id);
    }

    public boolean employeeExists(Long id) {

        return repository.existsById(id);
    }

    public List<Employee> getEmployeesByIds(List<Long> ids) {

        return repository.findAllById(ids);
    }

    public long getEmployeeCount() {

        return repository.count();
    }

    public void deleteEmployee(Long id) {

        repository.deleteById(id);
    }
}
```
Command Line Runner 
```java
package com.example.employee;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeRunner implements CommandLineRunner {

    private final EmployeeRepository repository;

    public EmployeeRunner(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        // ---------------------------------------------------------
        // 1. save()
        // ---------------------------------------------------------

        Employee e1 =
                new Employee("Aniket", "IT", 75000);

        Employee savedEmployee = repository.save(e1);

        System.out.println("1. save()");
        System.out.println(savedEmployee);

        // Expected:
        // 1. save()
        // Employee{id=1, name='Aniket', department='IT', salary=75000.0}


        // ---------------------------------------------------------
        // 2. saveAll()
        // ---------------------------------------------------------

        Employee e2 =
                new Employee("Rahul", "HR", 65000);

        Employee e3 =
                new Employee("Priya", "Finance", 80000);

        List<Employee> savedEmployees =
                repository.saveAll(List.of(e2, e3));

        System.out.println("\n2. saveAll()");
        savedEmployees.forEach(System.out::println);

        // Expected:
        // 2. saveAll()
        // Employee{id=2, name='Rahul', department='HR', salary=65000.0}
        // Employee{id=3, name='Priya', department='Finance', salary=80000.0}


        // ---------------------------------------------------------
        // 3. findById()
        // ---------------------------------------------------------

        repository.findById(1L)
                .ifPresent(employee -> {
                    System.out.println("\n3. findById()");
                    System.out.println(employee);
                });

        // Expected:
        // 3. findById()
        // Employee{id=1, name='Aniket', department='IT', salary=75000.0}


        // ---------------------------------------------------------
        // 4. existsById()
        // ---------------------------------------------------------

        boolean exists = repository.existsById(1L);

        System.out.println("\n4. existsById()");
        System.out.println(exists);

        // Expected:
        // 4. existsById()
        // true


        // ---------------------------------------------------------
        // 5. findAll()
        // ---------------------------------------------------------

        List<Employee> employees = repository.findAll();

        System.out.println("\n5. findAll()");
        employees.forEach(System.out::println);

        // Expected:
        // 5. findAll()
        // Employee{id=1, name='Aniket', department='IT', salary=75000.0}
        // Employee{id=2, name='Rahul', department='HR', salary=65000.0}
        // Employee{id=3, name='Priya', department='Finance', salary=80000.0}


        // ---------------------------------------------------------
        // 6. findAllById()
        // ---------------------------------------------------------

        List<Employee> selectedEmployees =
                repository.findAllById(List.of(1L, 3L));

        System.out.println("\n6. findAllById()");
        selectedEmployees.forEach(System.out::println);

        // Expected:
        // 6. findAllById()
        // Employee{id=1, name='Aniket', department='IT', salary=75000.0}
        // Employee{id=3, name='Priya', department='Finance', salary=80000.0}


        // ---------------------------------------------------------
        // 7. count()
        // ---------------------------------------------------------

        long count = repository.count();

        System.out.println("\n7. count()");
        System.out.println(count);

        // Expected:
        // 7. count()
        // 3


        // ---------------------------------------------------------
        // 8. deleteById()
        // ---------------------------------------------------------

        repository.deleteById(2L);

        System.out.println("\n8. deleteById()");
        System.out.println("Employee with ID 2 deleted");

        // Expected:
        // 8. deleteById()
        // Employee with ID 2 deleted


        // ---------------------------------------------------------
        // 9. delete()
        // ---------------------------------------------------------

        Employee employeeToDelete =
                repository.findById(3L).orElseThrow();

        repository.delete(employeeToDelete);

        System.out.println("\n9. delete()");
        System.out.println("Employee with ID 3 deleted");

        // Expected:
        // 9. delete()
        // Employee with ID 3 deleted


        // ---------------------------------------------------------
        // 10. deleteAllById()
        // ---------------------------------------------------------

        repository.deleteAllById(List.of(1L));

        System.out.println("\n10. deleteAllById()");
        System.out.println("Employee with ID 1 deleted");

        // Expected:
        // 10. deleteAllById()
        // Employee with ID 1 deleted


        // ---------------------------------------------------------
        // 11. deleteAll()
        // ---------------------------------------------------------

        repository.deleteAll();

        System.out.println("\n11. deleteAll()");
        System.out.println("All employees deleted");

        // Expected:
        // 11. deleteAll()
        // All employees deleted
    }
}
```