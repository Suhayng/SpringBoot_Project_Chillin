package com.chillin.service;

import com.chillin.dto.BoardComplainDTO;
import com.chillin.dto.ComplainManageDTO;
import com.chillin.dto.RepComplainDTO;
import com.chillin.repository.board.BoardRepository;
import com.chillin.repository.complain.BoardComplainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardComplainServiceImpl implements BoardComplainService{

    private final BoardComplainRepository bcRepository;

    @Override
    @Transactional
    public void insertBoardComplain(BoardComplainDTO dto,Long uid) {
        boolean prevWrite = bcRepository.prevBoardWrite(dto.getBid(),uid);

        if(prevWrite){
            boolean blind = bcRepository.write(dto,uid,true); // completed 를 강제로 1로 둬서 블라인드를 주지는 않게
            if(blind){
                /*블라인드 하는 행위*/
            }
        }else {
            bcRepository.write(dto,uid,false); // complete 를 0 으로 둬서 5개 쌓이면 블라인드 주게
        }

    }

    @Override
    public void insertRepComplain(RepComplainDTO dto, Long uid) {
        boolean prevWrite = bcRepository.prevRepWrite(dto.getRid(),uid);

        if(prevWrite){
            boolean blind = bcRepository.write(dto,uid,true); // completed 를 강제로 1로 둬서 블라인드를 주지는 않게
            if(blind){
                /*블라인드 하는 행위*/
            }
        }else {
            bcRepository.write(dto,uid,false); // complete 를 0 으로 둬서 5개 쌓이면 블라인드 주게
        }

    }

    @Override
    public List<ComplainManageDTO> getBoardList(int page, int pageSize,String type) {

        int startRow = (page - 1)*pageSize;

        List<ComplainManageDTO> list =  bcRepository.getBoardList(startRow,pageSize,type);

        return list;
    }

    @Override
    public void blinding(Long bid, String action) {
        bcRepository.blinding(bid,action);
    }

    @Override
    public void completing(Long cid) {
        bcRepository.completing(cid);
    }
}
