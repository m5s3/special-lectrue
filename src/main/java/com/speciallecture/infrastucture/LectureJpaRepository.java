package com.speciallecture.infrastucture;

import com.speciallecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long>, LectureRepository {

}
