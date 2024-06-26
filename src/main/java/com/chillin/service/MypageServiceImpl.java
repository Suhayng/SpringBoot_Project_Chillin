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

        for(Message item:messageList){
            MessageDTO dto = new MessageDTO();

            dto.setMeid(item.getMessageId());
            dto.setContent(item.getContent());
            dto.setTime(item.getTime());
            dto.setIs_read(item.getIsRead());
            dto.setSender(item.getSender().getUserId());
            dto.setReceiver(item.getReceiver().getUserId());
            dto.setSenderNickName(item.getSender().getNickname());
            dto.setReceiverNickName(item.getReceiver().getNickname());

            if(userId == item.getSender().getUserId()){
                dto.setCheck(true);
            }else {
                dto.setCheck(false);
            }

            dtolist.add(dto);

        }

        return dtolist;
    }

    /**쪽지 상세페이지 목록*/
    @Override
    public List<MessageDTO> getMessageDetailList(Long userId, Long messageId) {

        List<Message> messageList = messageRepository.getMessageDetailList(userId, messageId);

        List<MessageDTO> dtolist = new ArrayList<>();

        for(Message item:messageList){
            MessageDTO dto = new MessageDTO();

            dto.setMeid(item.getMessageId());
            dto.setContent(item.getContent());
            dto.setTime(item.getTime());
            dto.setIs_read(item.getIsRead());
            dto.setSender(item.getSender().getUserId());
            dto.setReceiver(item.getReceiver().getUserId());
            dto.setSenderNickName(item.getSender().getNickname());
            dto.setReceiverNickName(item.getReceiver().getNickname());

            if(userId == item.getSender().getUserId()){
                dto.setCheck(true);
            }else {
                dto.setCheck(false);
            }

            dtolist.add(dto);

        }
        return dtolist;
    }

    /**쪽지 보내기*/
    @Override
    public Long writeMessage(MessageDTO dto) {
        messageRepository.writeMessage(dto);

        return dto.getSender();

    }

    /**북마크 리스트*/
    @Override
    public List<BoardDTO> getBookmarkList(Long userId) {
        List<BoardDTO> bookmarkList = boardRepository.getBookmarkList(userId);

        return bookmarkList;
    }

    /**작성한 글 리스트*/
    @Override
    public List<BoardDTO> getMyBoardList(Long userId) {
        List<BoardDTO> myList = boardRepository.getMyBoardList(userId);
        return myList;
    }


}