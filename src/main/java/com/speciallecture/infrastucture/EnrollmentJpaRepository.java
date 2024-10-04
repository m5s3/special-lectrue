package com.speciallecture.infrastucture;

import com.speciallecture.domain.Enrollment;
import com.speciallecture.domain.Lecture;
import com.speciallecture.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentJpaRepository extends JpaRepository<Enrollment, Long>, EnrollmentRepository {

    int countEnrollmentByLecture(Lecture lecture);
    boolean existsByLectureAndStudent(Lecture lecture, Student student);

    @Override
    Enrollment save(Enrollment enrollment);
}
