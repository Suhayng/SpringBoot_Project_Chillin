package com.chillin.service;

import com.chillin.domain.Board;
import com.chillin.domain.User;
import com.chillin.dto.BoardDTO;
import com.chillin.dto.UserDTO;
import com.chillin.repository.board.BoardRepository;
import com.chillin.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    /** 회원 리스트 */
    @Override
    public List<UserDTO> getUserList() {
        List<User> users = userRepository.findAll();

        List<UserDTO> dtoList = users.stream()
                .map(item -> modelMapper.map(item, UserDTO.class))
                .collect(Collectors.toList());

        return dtoList;
    }

    /** 회원 별 게시판 */
    @Override
    public List<BoardDTO> getUserBoardList(Long uid) {
        List<BoardDTO> boardDTOS = boardRepository.getUserBoardList(uid);
        return boardDTOS;
    }
}
