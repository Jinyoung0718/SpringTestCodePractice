package com.sjy.dayonetest;

import com.sjy.dayonetest.model.StudentScoreFixture;
import com.sjy.dayonetest.repository.StudentScoreRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DayontestApplicationTests extends IntegrationTest {

    @Autowired
    private StudentScoreRepository studentScoreRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void contextLoads() {
        var StudentScore = StudentScoreFixture.passed();
        var savedStudentScore = studentScoreRepository.save(StudentScore);

        entityManager.flush();
        entityManager.clear();

        var queryStudentScore = studentScoreRepository.findById(savedStudentScore.getId()).orElseThrow();


        Assertions.assertEquals(StudentScore.getId(), queryStudentScore.getId());
        Assertions.assertEquals(StudentScore.getStudentName(), queryStudentScore.getStudentName());
        Assertions.assertEquals(StudentScore.getMathScore(), queryStudentScore.getMathScore());
        Assertions.assertEquals(StudentScore.getEnglishScore(), queryStudentScore.getEnglishScore());
        Assertions.assertEquals(StudentScore.getKorScore(), queryStudentScore.getKorScore());
    }
}
