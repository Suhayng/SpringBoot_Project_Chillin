package com.chillin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDTO {

    private Long nid;
    private String title;
    private String link;
    private String publisher;
    private LocalDateTime writeDate;

}
