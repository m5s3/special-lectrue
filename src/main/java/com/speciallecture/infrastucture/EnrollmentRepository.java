package com.speciallecture.infrastucture;

import com.speciallecture.domain.Enrollment;
import com.speciallecture.domain.Lecture;
import com.speciallecture.domain.Student;
import com.speciallecture.infrastucture.dto.EnrollmentDto;
import jakarta.persistence.Tuple;
import java.util.List;

public interface EnrollmentRepository {
    int countEnrollmentByLecture(Lecture lecture);
    boolean existsByLectureAndStudent(Lecture lecture, Student student);
    Enrollment save(Enrollment enrollment);
    List<Tuple> findByStudent(List<Long> lectureIds, long studentId);
}
