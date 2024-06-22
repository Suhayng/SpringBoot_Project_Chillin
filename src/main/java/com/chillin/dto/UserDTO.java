package com.chillin.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {


    private Long userId;

    @NotEmpty(message="이메일을 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식으로 입력하세요")
    private String id;

    @NotEmpty(message="패스워드를 입력하세요")
    @Size(min = 4, max=10, message = "비밀번호는 4자이상 10자 이하여야 합니다.")
    private String password;


    @NotEmpty(message = "닉네임을 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$"
            ,message = "닉네임은 숫자, 한글, 영문자만 가능합니다.")
    private String nickName;

    @NotEmpty(message = "이름을 입력하세요")
    @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = "이름은 한글 또는 영문자만 가능합니다")
    private String name;


}
