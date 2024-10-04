package com.speciallecture.infrastucture;

import com.speciallecture.domain.Lecture;
import com.speciallecture.domain.Student;
import java.util.List;
import java.util.Optional;

public interface LectureRepository {

    Optional<Lecture> findById(Long id);
    List<Lecture> findAll();

    List<Lecture> findAvailableLectures();
}
