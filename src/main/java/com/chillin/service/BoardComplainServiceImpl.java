package com.chillin.service;

import com.chillin.dto.BoardComplainDTO;
import com.chillin.repository.board.BoardRepository;
import com.chillin.repository.complain.BoardComplainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardComplainServiceImpl implements BoardComplainService{

    private final BoardComplainRepository bcRepository;

    @Override
    @Transactional
    public void insertComplain(BoardComplainDTO dto,Long uid) {
        boolean prevWrite = bcRepository.prevWrite(dto.getBid(),uid);

        if(prevWrite){
            boolean blind = bcRepository.write(dto,uid,true); // completed 를 강제로 1로 둬서 블라인드를 주지는 않게
            if(blind){
                /*블라인드 하는 행위*/
            }
        }else {
            bcRepository.write(dto,uid,false); // complete 를 0 으로 둬서 5개 쌓이면 블라인드 주게
        }

    }
}
