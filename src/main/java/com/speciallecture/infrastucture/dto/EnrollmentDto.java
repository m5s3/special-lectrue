package com.speciallecture.infrastucture.dto;

import jakarta.persistence.Tuple;

public record EnrollmentDto(
        long id,
        long lectureId,
        long studentId
) {

    public static EnrollmentDto fromTuple(Tuple t) {
        return new EnrollmentDto(Long.parseLong(t.get("id").toString()), Long.parseLong(t.get("lectureId").toString()),
                Long.parseLong(
                        t.get("studentId").toString()));
    }
}
