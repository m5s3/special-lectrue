package com.speciallecture.infrastucture;

import com.speciallecture.domain.Student;
import java.util.Optional;

public interface StudentRepository {
    Optional<Student> findById(Long id);
}
