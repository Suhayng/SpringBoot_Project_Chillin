package com.chillin.service;

import com.chillin.domain.Board;
import com.chillin.domain.Rep;
import com.chillin.domain.User;
import com.chillin.dto.RepDTO;
import com.chillin.repository.rep.RepRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .blind(false)
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

    @Override
    @Transactional
    public Map<String, Object> boomup(Long rid, Long uid) {
        Map<String, Object> map = new HashMap<>();


        if(uid != null) {
            String status = repRepository.getStatusMine(rid,uid);
            if("up".equals(status)){
                /* 삭제 */
                repRepository.deleteBoom(rid,uid);
                map.put("status","no");

            }else if("down".equals(status)){
                /* update -> true */
                repRepository.updateBoom(rid,uid,true);
                map.put("status","up");

            }else if("no".equals(status)){
                /* insert -> true */
                repRepository.insertBoom(rid,uid,true);
                map.put("status","up");

            }else{
                map.put("status","fail");
            }

        }else{
            map.put("status","fail");
        }
        repRepository.getOneRepBoom(rid,map);
        return map;
    }

    @Override
    public Map<String, Object> boomdown(Long rid, Long uid) {
        Map<String, Object> map = new HashMap<>();


        if(uid != null) {
            String status = repRepository.getStatusMine(rid,uid);
            if("up".equals(status)){
                repRepository.updateBoom(rid,uid,false);
                map.put("status","down");

            }else if("down".equals(status)){
                repRepository.deleteBoom(rid,uid);
                map.put("status","no");

            }else if("no".equals(status)){
                repRepository.insertBoom(rid,uid,false);
                map.put("status","down");

            }else{
                map.put("status","fail");
            }

        }else{
            map.put("status","fail");
        }
        repRepository.getOneRepBoom(rid,map);
        return map;
    }
}
