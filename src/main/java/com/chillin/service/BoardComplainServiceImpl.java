package com.chillin.service;

import com.chillin.dto.BoardComplainDTO;
import com.chillin.dto.ComplainManageDTO;
import com.chillin.dto.RepComplainDTO;
import com.chillin.repository.board.BoardRepository;
import com.chillin.repository.complain.BoardComplainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardComplainServiceImpl implements BoardComplainService{

    private final BoardComplainRepository bcRepository;

    @Override
    @Transactional
    public void insertBoardComplain(BoardComplainDTO dto,Long uid) {
        boolean prevWrite = bcRepository.prevBoardWrite(dto.getBid(), uid);

        if (prevWrite) {
            bcRepository.write(dto, uid, true); // completed 를 강제로 1로 둬서 블라인드를 주지는 않게

        } else {
            boolean blind = bcRepository.write(dto, uid, false); // complete 를 0 으로 둬서 5개 쌓이면 블라인드 주게


            System.out.println("============================");
            System.out.println("============================");
            //boardSlack(dto.getBid(),dto.getReason(),dto.getDetail());
            System.out.println("============================");
            System.out.println("============================");

            if (blind) {
                /*블라인드 하는 행위*/
                blinding(dto.getBid(), "do");
            }

        }
    }
    /** 슬랙으로 보내는 행위 */
    private void boardSlack(Long bid,String reason, String detail) {
        try {
            String pythonExe = "C:\\Users\\db400tea\\AppData\\Local\\Programs\\Python\\Python312\\python.exe";
            String pythonScriptPath = "C:\\Users\\db400tea\\Desktop\\CHILLIN\\SpringBoot_Project_Chillin\\chillin\\src\\main\\resources\\static\\python\\slack.py";
            String goToBoard = "localhost:8080/community/"+bid;
            ProcessBuilder processBuilder = new ProcessBuilder(pythonExe, pythonScriptPath,goToBoard,reason,detail);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 프로세스 종료 코드 확인
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void insertRepComplain(RepComplainDTO dto, Long uid) {
        boolean prevWrite = bcRepository.prevRepWrite(dto.getRid(),uid);

        if(prevWrite){
            bcRepository.write(dto,uid,true); // completed 를 강제로 1로 둬서 블라인드를 주지는 않게

        }else {
            boolean blind = bcRepository.write(dto,uid,false); // complete 를 0 으로 둬서 5개 쌓이면 블라인드 주게
            if(blind){
                repBlinding(dto.getRid());
            }
        }

    }

    private void repBlinding(Long rid){
        bcRepository.repBlindingRid(rid);
    }

    @Override
    public List<ComplainManageDTO> getBoardList(int page, int pageSize,String type) {

        int startRow = (page - 1)*pageSize;

        List<ComplainManageDTO> list =  bcRepository.getBoardList(startRow,pageSize,type);

        return list;
    }


    @Override
    public List<ComplainManageDTO> boardUnionComplain(Long bid) {
        List<ComplainManageDTO> list =  bcRepository.getBoardUnionList(bid);

        return list;
    }

    @Override
    public List<ComplainManageDTO> repUnionComplain(Long cid) {
        List<ComplainManageDTO> list =  bcRepository.getRepUnionList(cid);

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

    @Override
    public List<ComplainManageDTO> getRepList(int page, int pageSize, String type) {

        int startRow = (page - 1)*pageSize;

        List<ComplainManageDTO> list =  bcRepository.getRepList(startRow,pageSize,type);

        return list;
    }

    @Override
    public void repBlinding(Long cid, String action) {
        bcRepository.repBlinding(cid,action);
        bcRepository.repCompleting(cid);
    }

    @Override
    public void repCompleting(Long cid) {
        bcRepository.repCompleting(cid);
    }

}
