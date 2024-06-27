package com.chillin.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoardDTO {
    private Long bid;
    private String title;
    private String content;

    private LocalDateTime writeDate;
    private LocalDateTime modifyDate;

    private Long uid;
    private String nickname;

    private Boolean success;


    private Integer boomup;
    private Integer boomdown;

    private String timeAgo;

}
