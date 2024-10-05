package com.sjy.dayonetest.service;

import com.sjy.dayonetest.controller.response.ExamFailStudentResponse;
import com.sjy.dayonetest.controller.response.ExamPassStudentResponse;
import com.sjy.dayonetest.model.*;
import com.sjy.dayonetest.repository.StudentFailRepository;
import com.sjy.dayonetest.repository.StudentPassRepository;
import com.sjy.dayonetest.repository.StudentScoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

public class StudentScoreServiceTest {

    private StudentScoreService studentScoreService;
    private StudentScoreRepository studentScoreRepository;
    private StudentPassRepository studentPassRepository;
    private StudentFailRepository studentFailRepository;

    @BeforeEach
    public void beforeEach() {
        studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        studentPassRepository = Mockito.mock(StudentPassRepository.class);
        studentFailRepository = Mockito.mock(StudentFailRepository.class);
        studentScoreService = new StudentScoreService(
                studentScoreRepository,
                studentPassRepository,
                studentFailRepository);
    }

    @Test
    @DisplayName("첫번째 Mock 테스트")
    public void firstSaveScoreMockTest() {
        // given
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

        StudentScore expectStudentScore = StudentScoreTestDataBuilder.passed().build();
        StudentPass expectStudentPass = StudentPassFixture.create(expectStudentScore);

        ArgumentCaptor<StudentScore> studentScoreArgumentCaptor = ArgumentCaptor.forClass(StudentScore.class);
        ArgumentCaptor<StudentPass> studentPassArgumentCaptor = ArgumentCaptor.forClass(StudentPass.class);

        // when
        studentScoreService.saveScore(
                expectStudentScore.getStudentName(),
                expectStudentScore.getExam(),
                expectStudentScore.getKorScore(),
                expectStudentScore.getEnglishScore(),
                expectStudentScore.getMathScore()
        );

        // then
        Mockito.verify(studentScoreRepository, Mockito.times(1)).save(studentScoreArgumentCaptor.capture());
        StudentScore capturedStudentScore = studentScoreArgumentCaptor.getValue();

        Assertions.assertEquals(expectStudentScore.getStudentName(), capturedStudentScore.getStudentName());
        Assertions.assertEquals(expectStudentScore.getExam(), capturedStudentScore.getExam());
        Assertions.assertEquals(expectStudentScore.getKorScore(), capturedStudentScore.getKorScore());
        Assertions.assertEquals(expectStudentScore.getEnglishScore(), capturedStudentScore.getEnglishScore());
        Assertions.assertEquals(expectStudentScore.getMathScore(), capturedStudentScore.getMathScore());

        Mockito.verify(studentPassRepository, Mockito.times(1)).save(studentPassArgumentCaptor.capture());
        StudentPass capturedStudentPass = studentPassArgumentCaptor.getValue();

        Assertions.assertEquals(expectStudentPass.getStudentName(), capturedStudentPass.getStudentName());
        Assertions.assertEquals(expectStudentPass.getExam(), capturedStudentPass.getExam());
        Assertions.assertEquals(expectStudentPass.getAvgScore(), capturedStudentPass.getAvgScore());

        Mockito.verify(studentFailRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    @DisplayName("성적 저장 로직 검증 / 60점 미만인 경우")
    public void saveScoreMockitoFailTest() {
        // given : 평균점수가 60점 미만인 경우

        StudentScore expectStudentScore = StudentScoreFixture.failed();
        StudentFail expectStudentFail = StudentFailFixture.create(expectStudentScore);

        ArgumentCaptor<StudentScore> studentScoreArgumentCaptor = ArgumentCaptor.forClass(StudentScore.class);
        ArgumentCaptor<StudentFail> studentFailArgumentCaptor = ArgumentCaptor.forClass(StudentFail.class);

        // when
        studentScoreService.saveScore(
                expectStudentScore.getStudentName(),
                expectStudentScore.getExam(),
                expectStudentScore.getKorScore(),
                expectStudentScore.getEnglishScore(),
                expectStudentScore.getMathScore()
        );

        // then

        Mockito.verify(studentScoreRepository, Mockito.times(1)).save(studentScoreArgumentCaptor.capture());
        StudentScore capturedStudentScore = studentScoreArgumentCaptor.getValue();

        Assertions.assertEquals(expectStudentScore.getStudentName(), capturedStudentScore.getStudentName());
        Assertions.assertEquals(expectStudentScore.getExam(), capturedStudentScore.getExam());
        Assertions.assertEquals(expectStudentScore.getKorScore(), capturedStudentScore.getKorScore());
        Assertions.assertEquals(expectStudentScore.getEnglishScore(), capturedStudentScore.getEnglishScore());
        Assertions.assertEquals(expectStudentScore.getMathScore(), capturedStudentScore.getMathScore());

        Mockito.verify(studentPassRepository, Mockito.times(0)).save(Mockito.any());

        Mockito.verify(studentFailRepository, Mockito.times(1)).save(studentFailArgumentCaptor.capture());
        StudentFail capturedStudentFail = studentFailArgumentCaptor.getValue();
        Assertions.assertEquals(expectStudentFail.getStudentName(), capturedStudentFail.getStudentName());
        Assertions.assertEquals(expectStudentFail.getExam(), capturedStudentFail.getExam());
        Assertions.assertEquals(expectStudentFail.getAvgScore(), capturedStudentFail.getAvgScore());
    }

    @Test
    @DisplayName("합격자 명단 가져오기 검증")
    public void getPassStudentsListTest() {
        // given

        String givenTestExam = "testExam";

        StudentPass expectStudent1 = StudentPassFixture.create("jinyoung", givenTestExam);
        StudentPass expectStudent2 = StudentPassFixture.create("testName", givenTestExam);
        StudentPass NotExpectStudent3 = StudentPassFixture.create("anotherTestName", "anotherTestExam");

        Mockito.when(studentPassRepository.findAll()).thenReturn(List.of(
                expectStudent1,
                expectStudent2,
                NotExpectStudent3
        ));

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
        String givenTestExam = "testExam";

        StudentFail NotExpectStudent = StudentFailFixture.create("jinyoung", "anotherTestExam");
        StudentFail expectStudent1 = StudentFailFixture.create("testName", givenTestExam);
        StudentFail expectStudent2 = StudentFailFixture.create("testName2", givenTestExam);

        Mockito.when(studentFailRepository.findAll()).thenReturn(List.of(
                expectStudent1,
                expectStudent2,
                NotExpectStudent
        ));

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
