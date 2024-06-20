package com.chillin.service;

import com.chillin.domain.User;
import com.chillin.dto.UserDTO;
import com.chillin.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private UserRepository repository;
    private ModelMapper modelMapper;

    @Autowired
    private UserServiceImpl(UserRepository repository, ModelMapper modelMapper){
        this.repository=repository;
        this.modelMapper=modelMapper;
    }
    /**회원가입*/
    @Override
    public Long join(UserDTO dto) {
        User user = modelMapper.map(dto, User.class);

        User result = repository.save(user);
        return result.getUserId();
    }
}
