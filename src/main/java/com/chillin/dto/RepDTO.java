package com.chillin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RepDTO {

    private Long rid;
    private Long bid;
    private Long uid;
    private LocalDateTime writeDate;
    private String content;

    private String id;

}
