package com.speciallecture.infrastucture;

import com.speciallecture.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentJpaRepository extends JpaRepository<Student, Long>, StudentRepository {

}
