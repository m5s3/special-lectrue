package com.speciallecture.application;

import com.speciallecture.application.dto.ResponseLectureDto;
import com.speciallecture.domain.Enrollment;
import com.speciallecture.domain.Lecture;
import com.speciallecture.domain.Student;
import com.speciallecture.infrastucture.EnrollmentRepository;
import com.speciallecture.infrastucture.LectureJpaRepository;
import com.speciallecture.infrastucture.LectureRepository;
import com.speciallecture.infrastucture.StudentRepository;
import com.speciallecture.infrastucture.dto.EnrollmentDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LectureService {

    private static final Logger log = LoggerFactory.getLogger(LectureService.class);

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LectureJpaRepository lectureJpaRepository;

    public void enroll(long lectureId, long userId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(IllegalArgumentException::new);
        Student student = studentRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
        int countOfEnrollment = enrollmentRepository.countEnrollmentByLecture(lecture);
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByLectureAndStudent(
                lecture, student);

        Enrollment enroll = lecture.enroll(student, enrollments, countOfEnrollment, LocalDateTime.now());
        enrollmentRepository.save(enroll);
    }

    @Transactional(readOnly = true)
    public List<ResponseLectureDto> showLectures() {
        List<Lecture> lectures = lectureRepository.findAll();
        return lectures.stream().map(ResponseLectureDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseLectureDto> showAvailableLectures(long userId) {
        Student student = studentRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
        List<Lecture> lectures = lectureRepository.findAvailableLectures();
        List<Long> lectureIds = lectures.stream().map(Lecture::getId)
                .toList();
        List<Long> enrolledLecturesIds = enrollmentRepository.findByStudent(lectureIds, student.getId())
                .stream()
                .map(EnrollmentDto::fromTuple)
                .map(EnrollmentDto::lectureId)
                .toList();

        return lectures.stream().filter(lecture -> !enrolledLecturesIds.contains(lecture.getId()))
                .map(ResponseLectureDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ResponseLectureDto> showAvailableLecturesFilteredByDate(long userId, LocalDateTime date) {
        Student student = studentRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
        List<Lecture> lectures = lectureRepository.findLecturesByStartDateIsLessThanEqualAndEndDateIsGreaterThan(date, LocalDateTime.now());

        List<Long> lectureIds = lectures.stream().map(Lecture::getId)
                .toList();
        List<Long> enrolledLecturesIds = enrollmentRepository.findByStudent(lectureIds, student.getId())
                .stream()
                .map(EnrollmentDto::fromTuple)
                .map(EnrollmentDto::lectureId)
                .toList();

        enrolledLecturesIds.stream().forEach(id -> log.info("id={}", id));

        return lectures.stream().filter(lecture -> !enrolledLecturesIds.contains(lecture.getId()))
                .map(ResponseLectureDto::fromEntity)
                .toList();
    }

}
