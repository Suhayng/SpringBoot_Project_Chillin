package com.chillin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {

    private Long newsId;
    private String title;
    private String link;
    private String publisher;
    private LocalDateTime writeDate;


}
