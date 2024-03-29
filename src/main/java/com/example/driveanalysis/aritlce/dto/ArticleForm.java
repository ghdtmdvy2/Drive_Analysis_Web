package com.example.driveanalysis.aritlce.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class ArticleForm {
    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max = 200, message = "제목을 200자 이하로 입력해주세요.")
    private String subject;
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
