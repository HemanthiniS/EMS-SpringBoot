
package com.example.demo.repository;

import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}


package com.example.demo.repository;

import com.example.demo.dto.DepartmentDTO;
import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT new com.example.demo.dto.DepartmentDTO(d.id, d.name, NULL) FROM Department d")
    List<DepartmentDTO> findDepartmentDTOs();

    @Query("SELECT d.id, d.name FROM Department d")
    List<Object[]> findDepartmentIdsAndNames();
}

