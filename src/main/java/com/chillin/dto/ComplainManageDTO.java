package com.chillin.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ComplainManageDTO {
    private Long cid;
    private Long target;/*bid임 ㅋ*/
    private Long rid;
    private String title;
    private Boolean blind;
    private Long uid;
    private String nickname;
    private String reason;
    private String detail;
    private Boolean complete;

}
