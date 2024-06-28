package com.chillin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoticeDTO {
    private Long noticeId;
    private String title;
    private String content;

    private LocalDateTime writeDate;
    private LocalDateTime modifyDate;

    private Boolean success;
}
