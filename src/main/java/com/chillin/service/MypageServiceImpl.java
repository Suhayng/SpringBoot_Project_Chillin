package com.chillin.service;

import com.chillin.domain.Board;
import com.chillin.domain.Message;
import com.chillin.domain.User;
import com.chillin.dto.BoardDTO;
import com.chillin.dto.MessageDTO;
import com.chillin.dto.UserDTO;
import com.chillin.repository.board.BoardRepository;
import com.chillin.repository.message.MessageRepository;
import com.chillin.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MypageServiceImpl implements MypageService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;


    /**
     * 유저 정보 가져오기
     */
    @Override
    public UserDTO getUser(Long userId) {
        User user = boardRepository.getUser(userId);

        UserDTO dto = modelMapper.map(user, UserDTO.class);

        return dto;
    }

    /**
     * 유저 정보 수정
     */
    @Override
    @Transactional
    public long modifyUser(UserDTO dto) {

        User user = boardRepository.getUser(dto.getUserId());

        if (user == null) {
            throw new RuntimeException("no user data!!!");

        } else {

            user.setId(dto.getId());
            user.setNickname(dto.getNickName());
            user.setName(dto.getName());
            if (dto.getPassword() != null && !dto.getPassword().equals("")) {
                user.setPassword(dto.getPassword());
            }

            return user.getUserId();
        }

    }

    /**
     * 유저 삭제
     */
    @Override
    @Transactional
    public long deleteUser(Long userId) {
        userRepository.deleteById(userId);

        return userId;
    }

    /**
     * 쪽지 리스트 가져오기
     */
    @Override
    public List<MessageDTO> getMessageList(Long userId) {
        List<Message> messageList = messageRepository.getMessageList(userId);

        List<MessageDTO> dtolist = new ArrayList<>();

        for (Message item : messageList) {
            MessageDTO dto = new MessageDTO();

            dto.setMeid(item.getMessageId());
            dto.setContent(item.getContent());
            dto.setTime(item.getTime());
            dto.setIs_read(item.getIsRead());
            dto.setSender(item.getSender().getUserId());
            dto.setReceiver(item.getReceiver().getUserId());
            dto.setSenderNickName(item.getSender().getNickname());
            dto.setReceiverNickName(item.getReceiver().getNickname());

            if (userId == item.getSender().getUserId()) {
                dto.setCheck(true);
            } else {
                dto.setCheck(false);
            }

            dtolist.add(dto);

        }

        return dtolist;
    }

    /**
     * 쪽지 상세페이지 목록
     */
    @Override
    public List<MessageDTO> getMessageDetailList(Long userId, Long messageId) {

        List<Message> messageList = messageRepository.getMessageDetailList(userId, messageId);

        List<MessageDTO> dtolist = new ArrayList<>();

        for (Message item : messageList) {
            MessageDTO dto = new MessageDTO();

            dto.setMeid(item.getMessageId());
            dto.setContent(item.getContent());
            dto.setTime(item.getTime());
            dto.setIs_read(item.getIsRead());
            dto.setSender(item.getSender().getUserId());
            dto.setReceiver(item.getReceiver().getUserId());
            dto.setSenderNickName(item.getSender().getNickname());
            dto.setReceiverNickName(item.getReceiver().getNickname());

            if (userId == item.getSender().getUserId()) {
                dto.setCheck(true);
            } else {
                dto.setCheck(false);
            }

            dtolist.add(dto);

        }
        return dtolist;
    }


    /**쪽지 읽음 처리*/
    @Override
    @Transactional
    public int setIsRead(Long userId, Long messageId) {

        //유저가 받은 메시지 중 안읽은 메시지 리스트
        List<Message> received = messageRepository.getMessageUnread(userId);

        if(received.size()>0){

            for (Message item : received) {
                if(item.getSender().getUserId().equals(messageId)){
                    item.setIsRead(true);
                    messageRepository.save(item);
                }
            }

        }


        return received.size();

    }

    @Override
    public boolean checkRead(Long userId) {
        //유저가 받은 메시지 중 안읽은 메시지 리스트
        List<Message> received = messageRepository.getMessageUnread(userId);

        if(received.size()>0){
            // 안읽은 메시지가 있으면 false
            return false;
        }else {
            // 다 읽었으면 true
            return true;
        }
    }

    /**
     * 쪽지 보내기
     */
    @Override
    public Long writeMessage(MessageDTO dto) {

        Optional<User> bySender = userRepository.findById(dto.getSender());
        Optional<User> byReceiver = userRepository.findById(dto.getReceiver());

// 값이 존재할 경우 해당 값을 사용하고, 값이 없을 경우 예외를 발생
        User sender = bySender.orElseThrow(() -> new NoSuchElementException("보낸사람이 존재하지 않습니다"));
        User receiver = byReceiver.orElseThrow(() -> new NoSuchElementException("받는사람이 존재하지 않습니다"));

        Message message = new Message();
        message.setContent(dto.getContent());
        message.setIsRead(false);
        message.setSender(sender);
        message.setReceiver(receiver);

        Message save = messageRepository.save(message);

        return save.getSender().getUserId();

    }

    /**
     * 북마크 리스트
     */
    @Override
    public List<BoardDTO> getBookmarkList(Long userId) {
        List<BoardDTO> bookmarkList = boardRepository.getBookmarkList(userId);

        return bookmarkList;
    }

    /**
     * 작성한 글 리스트
     */
    @Override
    public List<BoardDTO> getMyBoardList(Long userId) {
        List<BoardDTO> myList = boardRepository.getMyBoardList(userId);
        return myList;
    }



}
