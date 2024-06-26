package com.chillin.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RepComplainDTO {

    private Long rcid;
    private String reason;
    private String detail;
    private Long rid;
    private Long uid;

    private Long return_board;

}
