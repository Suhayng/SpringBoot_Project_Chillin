package com.chillin.service;

import com.chillin.domain.User;
import com.chillin.dto.UserDTO;
import com.chillin.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;


    @Override
    public UserDTO getUser(Long userId) {
        User user = boardRepository.getUser(userId);

        UserDTO dto = modelMapper.map(user, UserDTO.class);

        return dto;
    }

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
}
