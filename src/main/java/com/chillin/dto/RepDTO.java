package com.chillin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RepDTO {

    private Long rid;
    private Long bid;
    private Long uid;
    private LocalDateTime writeDate;
    private String content;

    private String nickname;


    private Integer boomup;
    private Integer boomdown;

    private String status;

    private Boolean blind;
}
