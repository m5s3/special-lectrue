package com.speciallecture.infrastucture;

import com.speciallecture.domain.Enrollment;
import com.speciallecture.domain.Lecture;
import com.speciallecture.domain.Student;
import jakarta.persistence.Tuple;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnrollmentJpaRepository extends JpaRepository<Enrollment, Long>, EnrollmentRepository {

    int countEnrollmentByLecture(Lecture lecture);
    boolean existsByLectureAndStudent(Lecture lecture, Student student);
    List<Enrollment> findEnrollmentsByLectureAndStudent(Lecture lecture, Student student);

    @Override
    Enrollment save(Enrollment enrollment);

    @Override
    @Query(value = "SELECT id,"
            + " lecture_id as lectureId,"
            + " student_id as studentId"
            + " FROM Enrollment"
            + " WHERE student_id = :studentId AND lecture_id IN (:lectureIds)", nativeQuery = true)
    List<Tuple> findByStudent(@Param("lectureIds") List<Long> lectureIds, @Param("studentId")long studentId);
}
