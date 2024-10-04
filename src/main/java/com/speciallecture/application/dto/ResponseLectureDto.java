package com.speciallecture.application.dto;

import com.speciallecture.domain.Lecture;
import java.time.LocalDateTime;

public record ResponseLectureDto(
        long id,
        String title,
        int capacity,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String lecturer
) {
    public static ResponseLectureDto fromEntity(Lecture lecture) {
        return new ResponseLectureDto(lecture.getId(), lecture.getTitle(), lecture.getCapacity(),
                lecture.getStartDate(), lecture.getEndDate(),
                lecture.getLecturer());
    }
}
