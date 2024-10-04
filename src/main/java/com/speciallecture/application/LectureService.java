package com.speciallecture.application;

import com.speciallecture.application.dto.ResponseLectureDto;
import com.speciallecture.domain.Enrollment;
import com.speciallecture.domain.Lecture;
import com.speciallecture.domain.Student;
import com.speciallecture.infrastucture.EnrollmentRepository;
import com.speciallecture.infrastucture.LectureRepository;
import com.speciallecture.infrastucture.StudentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public void enroll(long lectureId, long userId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(IllegalArgumentException::new);
        Student student = studentRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
        int countOfEnrollment = enrollmentRepository.countEnrollmentByLecture(lecture);
        Enrollment enroll = lecture.enroll(student, countOfEnrollment, LocalDateTime.now());
        enrollmentRepository.save(enroll);
    }

    @Transactional(readOnly = true)
    public List<ResponseLectureDto> showLectures() {
        List<Lecture> lectures = lectureRepository.findAll();
        return lectures.stream().map(ResponseLectureDto::fromEntity)
                .collect(Collectors.toList());
    }
}
