package com.chillin.service;

import com.chillin.domain.Board;
import com.chillin.domain.Rep;
import com.chillin.domain.User;
import com.chillin.dto.RepDTO;
import com.chillin.repository.rep.RepRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepServiceImpl implements RepService{

    private final RepRepository repRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<RepDTO> getReps(Long bid) {
        List<RepDTO> list = repRepository.getReps(bid);

        return list;
    }

    @Override
    public Long insertRep(RepDTO dto) {

        User user = repRepository.getUserByUid(dto.getUid());
        Board board = repRepository.getBoardByBid(dto.getBid());
        Rep rep = Rep.builder()
                .content(dto.getContent())
                .board(board)
                .user(user)
                .build();
        Rep saved = repRepository.save(rep);
        dto.setRid(saved.getRepId());
        dto.setWriteDate(saved.getWriteDate());
        dto.setNickname(saved.getUser().getNickname());
        return saved.getRepId();
    }

    @Override
    public boolean repDelete(Long rid, Long uid) {
        boolean delete_success = false;
        Rep rep = repRepository.findById(rid).orElseThrow(RuntimeException::new);
        if(rep.getUser().getUserId().equals(uid)){
            repRepository.delete(rep);
            delete_success = true;
        }
        return delete_success;
    }

    @Override
    public List<RepDTO> getReps2(Long bid, Long uid) {
        List<RepDTO> list = repRepository.getReps2(bid,uid);

        return list;
    }
}
