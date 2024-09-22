package com.sjy.dayontest.service;

import com.sjy.dayontest.controller.response.ExamFailStudentResponse;
import com.sjy.dayontest.controller.response.ExamPassStudentResponse;
import com.sjy.dayontest.model.StudentFail;
import com.sjy.dayontest.model.StudentPass;
import com.sjy.dayontest.repository.StudentFailRepository;
import com.sjy.dayontest.repository.StudentPassRepository;
import com.sjy.dayontest.repository.StudentScoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class StudentScoreServiceTest {

    @Test
    @DisplayName("첫번째 Mock 테스트")
    public void firstSaveScoreMockTest() {
        // given
        StudentScoreService studentScoreService = new StudentScoreService(
                Mockito.mock(StudentScoreRepository.class),
                Mockito.mock(StudentPassRepository.class),
                Mockito.mock(StudentFailRepository.class)
        );

        String givenStudentName = "John";
        String givenExam = "testExam";
        Integer givenKorScore = 80;
        Integer givenEnglishScore = 100;
        Integer givenMathScore = 60;

        // when
        studentScoreService.saveScore(
                givenStudentName,
                givenExam,
                givenKorScore,
                givenEnglishScore,
                givenMathScore
        );
    }

    @Test
    @DisplayName("성적 저장 로직 검증 / 60점 이상인 경우")
    public void saveScoreMockitoTest() {
        // given : 평균점수가 60점 이상인 경우
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        StudentScoreService studentScoreService = new StudentScoreService(
                studentScoreRepository,
                studentPassRepository,
                studentFailRepository
        );

        String givenStudentName = "John";
        String givenExam = "testExam";
        Integer givenKorScore = 80;
        Integer givenEnglishScore = 100;
        Integer givenMathScore = 60;

        // when
        studentScoreService.saveScore(
                givenStudentName,
                givenExam,
                givenKorScore,
                givenEnglishScore,
                givenMathScore
        );

        // then
        Mockito.verify(studentScoreRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(studentPassRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(studentFailRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    @DisplayName("성적 저장 로직 검증 / 60점 미만인 경우")
    public void saveScoreMockitoFailTest() {
        // given : 평균점수가 60점 미만인 경우
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        StudentScoreService studentScoreService = new StudentScoreService(
                studentScoreRepository,
                studentPassRepository,
                studentFailRepository
        );

        String givenStudentName = "John";
        String givenExam = "testExam";
        Integer givenKorScore = 40;
        Integer givenEnglishScore = 60;
        Integer givenMathScore = 60;

        // when
        studentScoreService.saveScore(
                givenStudentName,
                givenExam,
                givenKorScore,
                givenEnglishScore,
                givenMathScore
        );

        // then
        Mockito.verify(studentScoreRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(studentPassRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(studentFailRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("합격자 명단 가져오기 검증")
    public void getPassStudentsListTest() {
        // given
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        StudentPass expectStudent1 = StudentPass.builder().id(1L).studentName("John").exam("testExam").avgScore(70.0).build();
        StudentPass expectStudent2 = StudentPass.builder().id(2L).studentName("test").exam("testExam").avgScore(80.0).build();
        StudentPass NotExpectStudent3 = StudentPass.builder().id(3L).studentName("iamNot").exam("secondexam").avgScore(90.0).build();

        Mockito.when(studentPassRepository.findAll()).thenReturn(List.of(
                expectStudent1,
                expectStudent2,
                NotExpectStudent3
        ));

        StudentScoreService studentScoreService = new StudentScoreService(
                studentScoreRepository,
                studentPassRepository,
                studentFailRepository
        );

        String givenTestExam = "testExam";

        // when
        var expectResponses = List.of(expectStudent1, expectStudent2)
                .stream()
                .map((pass) -> new ExamPassStudentResponse(pass.getStudentName(), pass.getAvgScore()))
                .toList();

        List<ExamPassStudentResponse> responses = studentScoreService.getPassStudentsList(givenTestExam);

        // then
        Assertions.assertIterableEquals(expectResponses, responses);

        expectResponses.forEach((response) -> {
            System.out.println(response.getStudentName() + " " + response.getAvgScore());
        });
        System.out.println("================");
        responses.forEach((response) -> {
            System.out.println(response.getStudentName() + " " + response.getAvgScore());
        });
    }

    @Test
    @DisplayName("불합격자 명단 가져오기 검증")
    public void getFailStudentsListTest() {
        // given
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        String givenTestExam = "testExam";

        StudentFail NotExpectStudent = StudentFail.builder().id(1L).studentName("John").exam("secondExam").avgScore(50.0).build();
        StudentFail expectStudent1 = StudentFail.builder().id(2L).studentName("test").exam(givenTestExam).avgScore(45.0).build();
        StudentFail expectStudent2 = StudentFail.builder().id(3L).studentName("iamNot").exam(givenTestExam).avgScore(35.0).build();

        Mockito.when(studentFailRepository.findAll()).thenReturn(List.of(
                expectStudent1,
                expectStudent2,
                NotExpectStudent
        ));

        StudentScoreService studentScoreService = new StudentScoreService(
                studentScoreRepository,
                studentPassRepository,
                studentFailRepository
        );

        // when
        var expectResponses = List.of(expectStudent1, expectStudent2)
                .stream()
                .map((fail) -> new ExamFailStudentResponse(fail.getStudentName(), fail.getAvgScore()))
                .toList();

        List<ExamFailStudentResponse> responses = studentScoreService.getFailStudentsList(givenTestExam);

        // then
        Assertions.assertIterableEquals(expectResponses, responses);
    }
}
