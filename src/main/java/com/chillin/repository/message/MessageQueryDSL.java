package com.chillin.repository.message;

import com.chillin.domain.Message;
import com.chillin.dto.MessageDTO;

import java.util.List;

public interface MessageQueryDSL {
    List<Message> getMessageList(Long userId);


}
