package com.chillin.service;

import com.chillin.domain.User;
import com.chillin.dto.UserDTO;
import com.chillin.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private ModelMapper modelMapper;

    @Autowired
    private UserServiceImpl(UserRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    /**
     * 로그인 시 받은 아이디로 회원 정보 DTO로 리턴
     */
    @Override
    public UserDTO findByEmail(String id) {
        User user = repository.findByEmail(id);
        UserDTO dto = new UserDTO();

        /*일치하는 아이디가 없을 때*/
        if (user == null || user.getUserId().equals("")) {
            return dto;
        }

        dto = modelMapper.map(user, UserDTO.class);

        return dto;
    }
/**회원가입시 닉네임 중복 체크*/
    @Override
    public Long nickNameCheck(String nickName) {
        Long result = repository.findByNickName(nickName);

        return result;
    }


    /**
     * 회원가입
     */
    @Override
    public Long join(UserDTO dto) {
        User user = modelMapper.map(dto, User.class);

        User result = repository.save(user);
        return result.getUserId();
    }


}
