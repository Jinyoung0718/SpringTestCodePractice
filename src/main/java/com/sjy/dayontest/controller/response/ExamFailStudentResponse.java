package com.sjy.dayontest.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class ExamFailStudentResponse {

    private final String studentName;
    private final Double avgScore;
}
