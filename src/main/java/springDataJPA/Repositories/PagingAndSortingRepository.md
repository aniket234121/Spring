# Paging And Sorting Repository
PagingAndSortingRepository<T, ID> is a Spring Data repository interface that provides methods for:

* Retrieving entities in pages
* Retrieving entities with sorting
* Combining pagination + sorting

It eliminates the need to manually write SQL such as:

```sql
SELECT * FROM employee
ORDER BY salary DESC
LIMIT 10 OFFSET 20;
```

Spring Data generates the required query based on the repository method you call.

Basic declaration
```java
public interface EmployeeRepository
extends PagingAndSortingRepository<Employee, Long> {
}
```
| Type Parameter | Meaning                        |
| -------------- |--------------------------------|
| `Employee`     | Entity type                    |
| `Long`         | Entity's ID type (Primary key) |

## Why Paging and Sorting?

Suppose the database contains 1 million employees.

You normally should not retrieve all 1 million records:

    employeeRepository.findAll();

Instead, retrieve a small portion:

    Page 0 → Employees 1–20
    Page 1 → Employees 21–40
    Page 2 → Employees 41–60

You can also control ordering:

    salary ASC
    salary DESC
    name ASC
    createdAt DESC

And combine both:

    Page 0
    20 employees
    sorted by salary DESC

## Sort Object

Sort represents the ordering of query results.

It answers:

In what order should the records be returned?

Create a Sort

    Sort sort = Sort.by("salary");

Default direction:

**ASC**

Explicit direction

    Sort sort = Sort.by(Sort.Direction.DESC, "salary");

Result:

**salary DESC**

### Multiple Sorting Fields

You can sort by multiple properties.

```java
Sort sort = Sort.by(
Sort.Order.desc("salary"),
Sort.Order.asc("name")
);

```
Equivalent SQL concept:

    ORDER BY salary DESC, name ASC

Using chained and()

    Sort sort = Sort.by("salary").descending().and(Sort.by("name").ascending());

## Pageable Object

Pageable represents the pagination request.

It answers:

Which page do I want, how many records per page, and optionally how should they be sorted?

The most common implementation is:

    PageRequest
Create Pageable

    Pageable pageable = PageRequest.of(0, 10);

Meaning:

    Page number = 0
    Page size   = 10

So:

    Page 0 → first 10 records
Important

Page numbers are zero-based.

    0 → first page
    1 → second page
    2 → third page

## Pageable with Sorting

You can combine pagination and sorting:

```java
Pageable pageable =
PageRequest.of(
0,
10,
Sort.by(Sort.Direction.DESC, "salary")
);

```
Meaning:

    Page     : 0
    Size     : 10
    Sort     : salary DESC

Conceptually:

    SELECT *
    FROM employee
    ORDER BY salary DESC
    LIMIT 10 OFFSET 0;

**Multiple sorting fields**

```java
Sort sort = Sort.by(
Sort.Order.desc("salary"),
Sort.Order.asc("name")
);

Pageable pageable =
PageRequest.of(0, 10, sort);

```
| Object        | Purpose                                             |
| ------------- | --------------------------------------------------- |
| `Sort`        | Defines ordering                                    |
| `Pageable`    | Defines page + size + optionally sorting            |
| `PageRequest` | Concrete implementation of `Pageable`               |
| `Page<T>`     | Contains the resulting page and pagination metadata |


### Relationship
    Sort
    └── Defines ordering
    
    Pageable
    ├── Page number
    ├── Page size
    └── Sort
    
    PageRequest
    └── Implementation of Pageable
    
    Page<T>
    ├── Current data
    ├── Total elements
    ├── Total pages
    ├── Current page
    └── Other pagination metadata
## PagingAndSortingRepository Methods

The exact inherited method set depends on the Spring Data version and interface hierarchy. For the core paging/sorting API, these are the important methods to know.

| Method                       | Purpose                            | Return Type   | Common Variation |
| ---------------------------- | ---------------------------------- | ------------- | ---------------- |
| `findAll()`                  | Retrieve all entities              | `Iterable<T>` | No arguments     |
| `findAll(Sort sort)`         | Retrieve all entities with sorting | `Iterable<T>` | `Sort`           |
| `findAll(Pageable pageable)` | Retrieve one page of entities      | `Page<T>`     | `Pageable`       |

### 1. findAll()
    Iterable<Employee> employees = repository.findAll();

Retrieves all entities.

Use when:

* Dataset is small
* Pagination isn't required
### 2. findAll(Sort sort)

    Sort sort = Sort.by(
    Sort.Direction.DESC,
    "salary"
    );

---

    Iterable<Employee> employees =
    repository.findAll(sort);

Retrieves all employees sorted by salary descending.

Conceptually:

    SELECT *
    FROM employee
    ORDER BY salary DESC;
### 3. findAll(Pageable pageable)


```java
    Pageable pageable =
    PageRequest.of(0, 10);

    Page<Employee> page =
    repository.findAll(pageable);
```

Retrieves one page.

You can also include sorting:

```java
Pageable pageable =
PageRequest.of(
0,
10,
Sort.by(Sort.Direction.DESC, "salary")
);
```


    Page<Employee> page =
    repository.findAll(pageable);

## Return Type — Page<T>

When using pagination, the important return type is:

    Page<Employee>

A Page contains both the actual records and pagination information.

For example:

    Page<Employee> page =
    employeeRepository.findAll(pageable);

You can retrieve the records:

    List<Employee> employees = page.getContent();

And pagination information:

    page.getNumber();
    page.getSize();
    page.getTotalElements();
    page.getTotalPages();
    page.hasNext();
    page.hasPrevious();

### Page Methods
| Method               | Purpose                              | Return Type |
| -------------------- | ------------------------------------ | ----------- |
| `getContent()`       | Current page's records               | `List<T>`   |
| `getNumber()`        | Current page number                  | `int`       |
| `getSize()`          | Requested page size                  | `int`       |
| `getTotalElements()` | Total records                        | `long`      |
| `getTotalPages()`    | Total pages                          | `int`       |
| `hasNext()`          | Checks next page                     | `boolean`   |
| `hasPrevious()`      | Checks previous page                 | `boolean`   |
| `isFirst()`          | Checks first page                    | `boolean`   |
| `isLast()`           | Checks last page                     | `boolean`   |
| `isEmpty()`          | Checks whether current page is empty | `boolean`   |

## Example
```java
package com.example.demo.entity;

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
package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeRepository
        extends PagingAndSortingRepository<Employee, Long> {
}
```
```java
package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }
}
```
```java
package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public Page<Employee> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Sort sort =
                Sort.by(
                        Sort.Direction.DESC,
                        "salary"
                );

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );

        return employeeService.getEmployees(pageable);
    }
}
```

Request

    GET /employees?page=0&size=10

Means:

    Page      = 0
    Page Size = 10
    Sort      = salary DESC

Another request:

    GET /employees?page=2&size=5

Means:

    Page      = 2
    Page Size = 5
    Sort      = salary DESC

Conceptually:

Records:


    1 ───────── 5       Page 0
    6 ───────── 10      Page 1
    11 ──────── 15      Page 2

