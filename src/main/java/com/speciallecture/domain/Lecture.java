package com.speciallecture.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "lecture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @Column(nullable = false)
    private int capacity;
    @Column(nullable = false, length = 50)
    private String lecturer;

    @Builder
    public Lecture(String title, String lecturer, LocalDateTime startDate, LocalDateTime endDate, int capacity) {
        this.title = title;
        this.lecturer = lecturer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.capacity = capacity;
    }

    public Enrollment enroll(Student student, int countOfEnrollment, LocalDateTime enrollmentDate) {
        validateEnroll(countOfEnrollment, enrollmentDate);
        return Enrollment.enroll(this, student, enrollmentDate);
    }

    public Enrollment enroll(Student student, List<Enrollment> enrollments, int countOfEnrollment,
            LocalDateTime enrollmentDate) {
        validateEnroll(student, enrollments, countOfEnrollment, enrollmentDate);
        return enroll(student, countOfEnrollment, enrollmentDate);
    }

    private void validateEnroll(Student student, List<Enrollment> enrollments, int countOfEnrollment, LocalDateTime enrollmentDate) {
        if (!enrollments.isEmpty()) {
            throw new IllegalArgumentException("해당 학생은 이미 강의를 신청 했습니다");
        }
        validateEnroll(countOfEnrollment, enrollmentDate);
    }

    private void validateEnroll(int countOfEnrollment, LocalDateTime enrollmentDate) {
        if (isGreaterThenCapacity(countOfEnrollment)) {
            throw new IllegalArgumentException("정원 수를 초과를 했습니다");
        }
        if (this.endDate.isBefore(enrollmentDate)) {
            throw new IllegalArgumentException("수강 신청 기간이 지났습니다");
        }
    }

    private boolean isGreaterThenCapacity(int countOfEnrollment) {
        return countOfEnrollment >= capacity;
    }
}
