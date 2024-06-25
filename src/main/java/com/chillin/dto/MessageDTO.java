package com.chillin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageDTO {
    private Long meid;
    private String content;
    private LocalDateTime time;
    private Boolean is_read;
    private Long sender;
    private String senderNickName;
    private Long receiver;
    private String receiverNickName;
    //로그인한 유저가 보낸사람이면 true, 받은 사람이면 false
    private Boolean check;

}
