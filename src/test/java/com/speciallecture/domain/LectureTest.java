package com.speciallecture.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LectureTest {

    private static final Logger log = LoggerFactory.getLogger(LectureTest.class);

    private Lecture 강의;

    @BeforeEach
    void setUp() {
        강의 = Lecture.builder()
                .title("테스트")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .capacity(30)
                .build();
    }


    @Test
    @DisplayName("수강 신청 인원이 30명 초과하면 예외가 발생한다")
    void apply_exception_when_capacity_exceed() {
        // Given
        int countOfStudent = 30;
        long studentId = 1;
        AtomicInteger countOfEnrollment = new AtomicInteger();

        // When
        while (countOfStudent > 0) {
            강의.enroll(new Student(studentId++), countOfEnrollment.getAndIncrement(), LocalDateTime.now());
            countOfStudent--;
        }

        // Then
        assertThatThrownBy(() -> 강의.enroll(new Student(31L), countOfEnrollment.getAndIncrement(), LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("수강 신청 기간이 지나서 등록을 하면 예외가 발생한다.")
    void apply_exception_over_endDate() {
        // Given
        AtomicInteger countOfEnrollment = new AtomicInteger();
        // When & Then
        assertThatThrownBy(() -> 강의.enroll(new Student(1L), countOfEnrollment.get(), LocalDateTime.now().plusDays(31)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("수강 신청을 중복으로 등록을 하면 예외가 발생한다.")
    void enroll_exception_duplication() {
        Enrollment enroll = 강의.enroll(new Student(1L), 1, LocalDateTime.now());

        assertThatThrownBy(() -> 강의.enroll(new Student(1L), List.of(enroll),  1, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}