package com.chillin.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoardComplainDTO {

    private Long bcid;
    private String reason;
    private String detail;
    private Long bid;
    private Long uid;

    private Long return_board;


}
