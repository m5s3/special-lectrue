package com.speciallecture.infrastucture;

import com.speciallecture.domain.Lecture;
import com.speciallecture.domain.Student;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long>, LectureRepository {

    @Query(value = "SELECT l.id, l.lecturer, l.start_date, l.end_date, l.capacity, l.title"
            + " From Lecture l"
            + " WHERE l.start_date <= now() and l.end_date >= now()",
    nativeQuery = true)
    List<Lecture> findAvailableLectures();
}
