package com.jolles.restfulservice.repository;

import com.jolles.restfulservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {

}
