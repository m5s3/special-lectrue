package com.speciallecture.infrastucture;

import com.speciallecture.domain.Enrollment;
import com.speciallecture.domain.Lecture;
import com.speciallecture.domain.Student;

public interface EnrollmentRepository {
    int countEnrollmentByLecture(Lecture lecture);
    boolean existsByLectureAndStudent(Lecture lecture, Student student);
    Enrollment save(Enrollment enrollment);
}
