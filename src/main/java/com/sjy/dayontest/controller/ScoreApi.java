package com.sjy.dayontest.controller;

import com.sjy.dayontest.controller.reqeust.SaveExamScoreRequest;
import com.sjy.dayontest.controller.response.ExamFailStudentResponse;
import com.sjy.dayontest.controller.response.ExamPassStudentResponse;
import com.sjy.dayontest.service.StudentScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ScoreApi {
    private final StudentScoreService studentScoreService;

    @PutMapping("/exam/{exam}/score")
    public void save(@PathVariable("exam") String exam, @RequestBody SaveExamScoreRequest request) {
        studentScoreService.saveScore(
                request.getStudentName(),
                exam,
                request.getKorScore(),
                request.getEnglishScore(),
                request.getMathScore());
    }

    @GetMapping("/exam/{exam}/pass")
    public List<ExamPassStudentResponse> pass(@PathVariable("exam") String exam) {
        return studentScoreService.getPassStudentsList(exam);
    }

    @GetMapping("/exam/{exam}/fail")
    public List<ExamFailStudentResponse> fail(@PathVariable("exam") String exam) {
        return studentScoreService.getFailStudentsList(exam);
    }
}