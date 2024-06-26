package com.chillin.service;

import com.chillin.repository.board.BoardRepository;
import com.chillin.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

}
