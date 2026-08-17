# Jpa Repository
JpaRepository<T, ID> is the main Spring Data JPA repository abstraction used when working with JPA entities.

It provides:

* CRUD operations
* List-based results
* Pagination
* Sorting
* JPA-specific operations
* Query-method support
* Query execution through JPA

```java
public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {
}
```
    Repository
    │
    ├── ListCrudRepository
    │       │
    │       └── CRUD + List
    │
    └── ListPagingAndSortingRepository
    │
    └── Paging + Sorting + List
    
    JpaRepository
    │
    ├── ListCrudRepository
    ├── ListPagingAndSortingRepository
    └── JPA-specific functionality

## JPA Methods
### Methods from ListCrudRepository and ListPagingAndSortingRepository
| Method                                      | Parameters               | Return Type       | Purpose                                               |
| ------------------------------------------- | ------------------------ | ----------------- | ----------------------------------------------------- |
| `save(S entity)`                            | `S entity`               | `<S extends T> S` | Saves a new entity or updates an existing entity      |
| `saveAll(Iterable<S> entities)`             | `Iterable<S>`            | `List<S>`         | Saves multiple entities                               |
| `findById(ID id)`                           | `ID`                     | `Optional<T>`     | Finds an entity by ID                                 |
| `existsById(ID id)`                         | `ID`                     | `boolean`         | Checks whether an entity exists                       |
| `findAll()`                                 | None                     | `List<T>`         | Retrieves all entities                                |
| `findAllById(Iterable<ID> ids)`             | `Iterable<ID>`           | `List<T>`         | Retrieves entities for the given IDs                  |
| `count()`                                   | None                     | `long`            | Returns the number of entities                        |
| `deleteById(ID id)`                         | `ID`                     | `void`            | Deletes an entity by ID                               |
| `delete(T entity)`                          | `T`                      | `void`            | Deletes the specified entity                          |
| `deleteAllById(Iterable<? extends ID> ids)` | `Iterable<? extends ID>` | `void`            | Deletes entities by IDs                               |
| `deleteAll(Iterable<? extends T> entities)` | `Iterable<? extends T>`  | `void`            | Deletes the specified entities                        |
| `deleteAll()`                               | None                     | `void`            | Deletes all entities                                  |
| `findAll(Sort sort)`                        | `Sort`                   | `List<T>`         | Retrieves all entities with sorting                   |
| `findAll(Pageable pageable)`                | `Pageable`               | `Page<T>`         | Retrieves a page with pagination and optional sorting |

### JPA Specific Methods
| Method                                   | Parameters     | Return Type       | Purpose                                                                          |
| ---------------------------------------- | -------------- | ----------------- | -------------------------------------------------------------------------------- |
| `flush()`                                | None           | `void`            | Flushes pending changes in the persistence context to the database               |
| `saveAndFlush(S entity)`                 | `S entity`     | `<S extends T> S` | Saves an entity and immediately flushes changes                                  |
| `saveAllAndFlush(Iterable<S> entities)`  | `Iterable<S>`  | `List<S>`         | Saves multiple entities and flushes changes                                      |
| `deleteAllInBatch(Iterable<T> entities)` | `Iterable<T>`  | `void`            | Deletes the supplied entities in a batch operation                               |
| `deleteAllByIdInBatch(Iterable<ID> ids)` | `Iterable<ID>` | `void`            | Deletes entities with the supplied IDs in a batch operation                      |
| `deleteAllInBatch()`                     | None           | `void`            | Deletes all entities in a batch operation                                        |
| `getReferenceById(ID id)`                | `ID id`        | `T`               | Returns a JPA reference/proxy without necessarily loading the entity immediately |

## Example
```java
package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }
}
```
```java
package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {
}
```
```java
package com.example.demo.runner;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmployeeRunner implements CommandLineRunner {

    private final EmployeeRepository repository;

    public EmployeeRunner(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        // =========================================================
        // 1. save()
        // =========================================================

        Employee e1 =
                repository.save(
                        new Employee(
                                "Aniket",
                                "IT",
                                75000
                        )
                );

        System.out.println("1. save()");
        System.out.println(e1);

        // Expected:
        // 1. save()
        // Employee{id=1, name='Aniket', department='IT', salary=75000.0}


        // =========================================================
        // 2. saveAll()
        // =========================================================

        List<Employee> savedEmployees =
                repository.saveAll(
                        List.of(
                                new Employee("Rahul", "HR", 55000),
                                new Employee("Priya", "Finance", 85000),
                                new Employee("Amit", "IT", 65000)
                        )
                );

        System.out.println("\n2. saveAll()");
        savedEmployees.forEach(System.out::println);

        // Expected:
        // 2. saveAll()
        // Employee{id=2, name='Rahul', department='HR', salary=55000.0}
        // Employee{id=3, name='Priya', department='Finance', salary=85000.0}
        // Employee{id=4, name='Amit', department='IT', salary=65000.0}


        // =========================================================
        // 3. findById()
        // =========================================================

        Optional<Employee> employee =
                repository.findById(e1.getId());

        System.out.println("\n3. findById()");
        employee.ifPresent(System.out::println);

        // Expected:
        // 3. findById()
        // Employee{id=1, name='Aniket', department='IT', salary=75000.0}


        // =========================================================
        // 4. existsById()
        // =========================================================

        boolean exists =
                repository.existsById(e1.getId());

        System.out.println("\n4. existsById()");
        System.out.println(exists);

        // Expected:
        // 4. existsById()
        // true


        // =========================================================
        // 5. findAll()
        // =========================================================

        List<Employee> employees =
                repository.findAll();

        System.out.println("\n5. findAll()");
        employees.forEach(System.out::println);

        // Expected:
        // 5. findAll()
        // Employee{id=1, name='Aniket', department='IT', salary=75000.0}
        // Employee{id=2, name='Rahul', department='HR', salary=55000.0}
        // Employee{id=3, name='Priya', department='Finance', salary=85000.0}
        // Employee{id=4, name='Amit', department='IT', salary=65000.0}


        // =========================================================
        // 6. findAllById()
        // =========================================================

        List<Employee> selectedEmployees =
                repository.findAllById(
                        List.of(1L, 3L)
                );

        System.out.println("\n6. findAllById()");
        selectedEmployees.forEach(System.out::println);

        // Expected:
        // 6. findAllById()
        // Employee{id=1, name='Aniket', department='IT', salary=75000.0}
        // Employee{id=3, name='Priya', department='Finance', salary=85000.0}


        // =========================================================
        // 7. count()
        // =========================================================

        long count =
                repository.count();

        System.out.println("\n7. count()");
        System.out.println(count);

        // Expected:
        // 7. count()
        // 4


        // =========================================================
        // 8. findAll(Sort)
        // =========================================================

        Sort sort =
                Sort.by("salary").descending();

        List<Employee> sortedEmployees =
                repository.findAll(sort);

        System.out.println("\n8. findAll(Sort)");
        sortedEmployees.forEach(System.out::println);

        // Expected:
        // 8. findAll(Sort)
        // Employee{id=3, name='Priya', department='Finance', salary=85000.0}
        // Employee{id=1, name='Aniket', department='IT', salary=75000.0}
        // Employee{id=4, name='Amit', department='IT', salary=65000.0}
        // Employee{id=2, name='Rahul', department='HR', salary=55000.0}


        // =========================================================
        // 9. findAll(Pageable)
        // =========================================================

        Pageable pageable =
                PageRequest.of(0, 2);

        Page<Employee> page =
                repository.findAll(pageable);

        System.out.println("\n9. findAll(Pageable)");

        page.getContent()
                .forEach(System.out::println);

        // Expected:
        // 9. findAll(Pageable)
        // First 2 employees are returned.


        // =========================================================
        // 10. Pagination + Sorting
        // =========================================================

        Pageable sortedPageable =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("salary").descending()
                );

        Page<Employee> sortedPage =
                repository.findAll(sortedPageable);

        System.out.println("\n10. Pagination + Sorting");

        sortedPage.getContent()
                .forEach(System.out::println);

        // Expected:
        // 10. Pagination + Sorting
        // Employee{id=3, name='Priya', department='Finance', salary=85000.0}
        // Employee{id=1, name='Aniket', department='IT', salary=75000.0}


        // =========================================================
        // 11. Page Information
        // =========================================================

        System.out.println("\n11. Page Information");

        System.out.println(
                "Page Number: " +
                sortedPage.getNumber()
        );

        System.out.println(
                "Page Size: " +
                sortedPage.getSize()
        );

        System.out.println(
                "Total Elements: " +
                sortedPage.getTotalElements()
        );

        System.out.println(
                "Total Pages: " +
                sortedPage.getTotalPages()
        );

        // Expected:
        // 11. Page Information
        // Page Number: 0
        // Page Size: 2
        // Total Elements: 4
        // Total Pages: 2


        // =========================================================
        // 12. saveAndFlush()
        // =========================================================

        Employee e5 =
                new Employee(
                        "Neha",
                        "HR",
                        60000
                );

        Employee savedAndFlushed =
                repository.saveAndFlush(e5);

        System.out.println("\n12. saveAndFlush()");
        System.out.println(savedAndFlushed);

        // Expected:
        // 12. saveAndFlush()
        // Employee{id=5, name='Neha', department='HR', salary=60000.0}

        // saveAndFlush():
        // save entity
        // +
        // immediately flush persistence context


        // =========================================================
        // 13. saveAllAndFlush()
        // =========================================================

        List<Employee> employeesToSave =
                List.of(
                        new Employee(
                                "Ravi",
                                "Finance",
                                90000
                        ),
                        new Employee(
                                "Sneha",
                                "IT",
                                70000
                        )
                );

        List<Employee> savedAndFlushedEmployees =
                repository.saveAllAndFlush(
                        employeesToSave
                );

        System.out.println("\n13. saveAllAndFlush()");

        savedAndFlushedEmployees
                .forEach(System.out::println);

        // Expected:
        // 13. saveAllAndFlush()
        // Employee{id=6, name='Ravi', department='Finance', salary=90000.0}
        // Employee{id=7, name='Sneha', department='IT', salary=70000.0}


        // =========================================================
        // 14. flush()
        // =========================================================

        repository.flush();

        System.out.println("\n14. flush()");
        System.out.println(
                "Persistence context flushed"
        );

        // Expected:
        // 14. flush()
        // Persistence context flushed


        // =========================================================
        // 15. getReferenceById()
        // =========================================================

        Employee reference =
                repository.getReferenceById(
                        e1.getId()
                );

        System.out.println("\n15. getReferenceById()");
        System.out.println(
                "Reference ID: " +
                reference.getId()
        );

        System.out.println(
                "Reference Name: " +
                reference.getName()
        );

        // Expected:
        // 15. getReferenceById()
        // Reference ID: 1
        // Reference Name: Aniket


        // =========================================================
        // 16. deleteById()
        // =========================================================

        repository.deleteById(2L);

        System.out.println("\n16. deleteById()");
        System.out.println(
                "Employee with ID 2 deleted"
        );

        // Expected:
        // 16. deleteById()
        // Employee with ID 2 deleted


        // =========================================================
        // 17. delete()
        // =========================================================

        Employee employeeToDelete =
                repository.findById(4L)
                        .orElseThrow();

        repository.delete(employeeToDelete);

        System.out.println("\n17. delete()");
        System.out.println(
                "Employee with ID 4 deleted"
        );

        // Expected:
        // 17. delete()
        // Employee with ID 4 deleted


        // =========================================================
        // 18. deleteAllById()
        // =========================================================

        repository.deleteAllById(
                List.of(5L)
        );

        System.out.println("\n18. deleteAllById()");
        System.out.println(
                "Employee with ID 5 deleted"
        );

        // Expected:
        // 18. deleteAllById()
        // Employee with ID 5 deleted


        // =========================================================
        // 19. deleteAll(Iterable)
        // =========================================================

        List<Employee> employeesToDelete =
                repository.findAllById(
                        List.of(6L, 7L)
                );

        repository.deleteAll(
                employeesToDelete
        );

        System.out.println("\n19. deleteAll(Iterable)");
        System.out.println(
                "Selected employees deleted"
        );

        // Expected:
        // 19. deleteAll(Iterable)
        // Selected employees deleted


        // =========================================================
        // 20. deleteAllInBatch(Iterable)
        // =========================================================

        Employee e8 =
                repository.save(
                        new Employee(
                                "Karan",
                                "IT",
                                72000
                        )
                );

        Employee e9 =
                repository.save(
                        new Employee(
                                "Pooja",
                                "HR",
                                58000
                        )
                );

        repository.deleteAllInBatch(
                List.of(e8, e9)
        );

        System.out.println(
                "\n20. deleteAllInBatch(Iterable)"
        );

        System.out.println(
                "Selected employees batch deleted"
        );

        // Expected:
        // 20. deleteAllInBatch(Iterable)
        // Selected employees batch deleted


        // =========================================================
        // 21. deleteAllByIdInBatch()
        // =========================================================

        Employee e10 =
                repository.save(
                        new Employee(
                                "Vikas",
                                "Finance",
                                68000
                        )
                );

        Employee e11 =
                repository.save(
                        new Employee(
                                "Meera",
                                "IT",
                                71000
                        )
                );

        repository.deleteAllByIdInBatch(
                List.of(
                        e10.getId(),
                        e11.getId()
                )
        );

        System.out.println(
                "\n21. deleteAllByIdInBatch()"
        );

        System.out.println(
                "Employees deleted by IDs in batch"
        );

        // Expected:
        // 21. deleteAllByIdInBatch()
        // Employees deleted by IDs in batch


        // =========================================================
        // 22. deleteAllInBatch()
        // =========================================================

        repository.deleteAllInBatch();

        System.out.println(
                "\n22. deleteAllInBatch()"
        );

        System.out.println(
                "All employees deleted in batch"
        );

        // Expected:
        // 22. deleteAllInBatch()
        // All employees deleted in batch


        // =========================================================
        // 23. Final count
        // =========================================================

        long finalCount =
                repository.count();

        System.out.println("\n23. Final count");
        System.out.println(finalCount);

        // Expected:
        // 23. Final count
        // 0
    }
}
```