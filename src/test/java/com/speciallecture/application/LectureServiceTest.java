package com.speciallecture.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.speciallecture.application.dto.ResponseLectureDto;
import com.speciallecture.domain.Lecture;
import com.speciallecture.domain.Student;
import com.speciallecture.infrastucture.EnrollmentRepository;
import com.speciallecture.infrastucture.LectureRepository;
import com.speciallecture.infrastucture.StudentRepository;
import com.speciallecture.utils.DatabaseCleanup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:data.sql")
class LectureServiceTest {

    private static final Logger log = LoggerFactory.getLogger(LectureServiceTest.class);

    @Autowired LectureService lectureService;
    @Autowired EnrollmentRepository enrollmentRepository;
    @Autowired LectureRepository lectureRepository;
    @Autowired StudentRepository studentRepository;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("수강 신청")
    void test_enroll() {
        // Given
        long studentId = 1L;
        long lectureId = 1L;
        Lecture lecture = lectureRepository.findById(lectureId).get();
        Student student = studentRepository.findById(studentId).get();

        // When
        lectureService.enroll(studentId, lectureId);

        // Then
        assertThat(enrollmentRepository.existsByLectureAndStudent(lecture, student)).isTrue();
    }

    @Test
    @DisplayName("수강 신청 인원을 초과하면 예외가 발생한다.")
    void test_enroll_full_capacity_exception() {
        // Given
        long lectureId = 1L;

        // When
        int countOfStudent = 30;
        while (countOfStudent > 0) {
            lectureService.enroll(lectureId, countOfStudent);
            countOfStudent--;
        }

        // Then
        assertThatThrownBy(() -> lectureService.enroll(lectureId, 31L))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("강의 목록을 가져온다.")
    void show_lectures() {
        // When
        List<ResponseLectureDto> responseLectureDtos = lectureService.showLectures();

        responseLectureDtos.forEach(System.out::println);

        // Then
        assertThat(responseLectureDtos).size().isEqualTo(6);
    }

    @Test
    @DisplayName("수강 가능한 강의 목록을 가져온다.")
    void show_avaliable_lectures() {
        // Given
        long studentId = 1L;
        long lectureId = 1L;

        // When
        lectureService.enroll(lectureId, studentId);
        List<ResponseLectureDto> responseLectureDtos = lectureService.showAvailableLectures(studentId);

        // Then
        assertThat(responseLectureDtos).size().isEqualTo(2);
    }

    @Test
    @DisplayName("수강 신청을 중복으로하면 예외가 발생한다.")
    void enroll_exception_duplication() {
        // Given
        long studentId = 1L;
        long lectureId = 1L;

        // When
        lectureService.enroll(studentId, lectureId);

        // Then
        assertThatThrownBy(() -> lectureService.enroll(studentId, lectureId)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("날짜 검색으로 수강 신청 가능한 강의를 가져온다.")
    void show_lectures_by_date() {
        // Given
        long studentId = 1L;
        long lectureId = 1L;

        // When
        lectureService.enroll(studentId, lectureId);
        List<ResponseLectureDto> responseLectureDtos = lectureService.showAvailableLecturesFilteredByDate(1L,
                LocalDateTime.of(2024, 10, 2, 0, 0, 0));

        // Then
        responseLectureDtos.forEach(responseLectureDto -> log.info("responseLectureDto: {}", responseLectureDto));
        assertThat(responseLectureDtos).size().isEqualTo(1);
    }
}