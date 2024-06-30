package com.chillin.controller;

import com.chillin.domain.Board;
import com.chillin.dto.BoardDTO;
import com.chillin.dto.MessageDTO;
import com.chillin.dto.UserDTO;
import com.chillin.service.BoardService;
import com.chillin.service.MypageService;
import com.chillin.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MypageController {

    private final MypageService mypageService;

    /**마이페이지-회원정보 수정으로 이동*/
    @GetMapping("/mypage/{userId}")
    public String mypageUser(@PathVariable Long userId, Model model){
        UserDTO dto = mypageService.getUser(userId);

        model.addAttribute("dto", dto);

        //안읽은 메시지 있는지 확인
        boolean checkRead = mypageService.checkRead(userId);
        model.addAttribute("checkRead", checkRead);

        return "mypage/mypage_modify";

    }

    /**회원정보 수정 결과*/
    @PostMapping("/user_modi")
    public String userModify(@ModelAttribute("dto") UserDTO dto){

            long userId = mypageService.modifyUser(dto);

            return "redirect:/mypage_modify_alert/"+userId;

    }

    /**회원정보 수정 알림 페이지*/
    @GetMapping("/mypage_modify_alert/{userId}")
    public String userModifyAlert(@PathVariable long userId, Model model){
        model.addAttribute("userId", userId);
        return "mypage/mypage_modify_alert";

    }

    /**마이페이지 - 회원 탈퇴하기*/
    @GetMapping("/delete_user/{userId}")
    public @ResponseBody long userDelete(@PathVariable Long userId){
        long id = mypageService.deleteUser(userId);

        log.info("..........delid.... {}", id);

        return id;
    }


    /**마이페이지-북마크로 이동*/
    @GetMapping("/mypage/bookmark/{userId}")
    public String mypageBookmark(@PathVariable Long userId, Model model){

        List<BoardDTO> list = mypageService.getBookmarkList(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("list", list);

        //안읽은 메시지 있는지 확인
        boolean checkRead = mypageService.checkRead(userId);
        model.addAttribute("checkRead", checkRead);

        return "mypage/mypage_bookmark";
    }


    /**마이페이지-쪽지로 이동*/
    @GetMapping("/mypage/message/{userId}")
    public String mypageMessage(@PathVariable Long userId, Model model){

        List<MessageDTO> list = mypageService.getMessageList(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("list", list);

        //안읽은 메시지 있는지 확인
        boolean checkRead = mypageService.checkRead(userId);
        model.addAttribute("checkRead", checkRead);


        return "mypage/mypage_message";
    }

    /**마이페이지-쪽지 상세페이지로 이동*/
    @GetMapping("/mypage/messagedetail/{userId}/{messageId}")
    public String mypageMessageDetail(@PathVariable Long userId
            , @PathVariable Long messageId, Model model
    ) {

        //userId는 로그인한 사용자 messageID는 상대방 계정

        // 전체 쪽지 목록 가져오기
        List<MessageDTO> list = mypageService.getMessageDetailList(userId, messageId);

        //list에서 받은사람이 userid인 것들은 다 읽음 처리 하기
        mypageService.setIsRead(userId, messageId);


        // 상대방 계정 닉네임 가져오기
        UserDTO messageUser = mypageService.getUser(messageId);
        String messageNickname = messageUser.getNickName();

        model.addAttribute("userId", userId);
        model.addAttribute("messageId", messageId);
        model.addAttribute("messageNickname", messageNickname);
        model.addAttribute("list", list);

        //안읽은 메시지 있는지 확인
        boolean checkRead = mypageService.checkRead(userId);
        model.addAttribute("checkRead", checkRead);

        return "mypage/mypage_message_detail";
    }

    /**마이페이지-쪽지 보내기*/
    @PostMapping("/write_message")
    public String writeMessage(@ModelAttribute("dto") MessageDTO dto, Model model){

        mypageService.writeMessage(dto);


        return "redirect:/message_alert/"+dto.getSender()+"/"+dto.getReceiver();
    }

    /**마이페이지-쪽지 송신 알림창*/
    @GetMapping("/message_alert/{userId}/{messageId}")
    public String writeMessageAlert(@PathVariable Long userId
            , @PathVariable Long messageId
            , Model model
    ){
        model.addAttribute("userId", userId);
        model.addAttribute("messageId", messageId);

        return "mypage/mypage_message_alert";
    }
    /**내가 쓴 글 보기*/
    @GetMapping("/mypage/board/{userId}")
    public String mypageBoard(@PathVariable Long userId, Model model){
        List<BoardDTO> list = mypageService.getMyBoardList(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("list", list);

        //안읽은 메시지 있는지 확인
        boolean checkRead = mypageService.checkRead(userId);
        model.addAttribute("checkRead", checkRead);

        return "mypage/mypage_boardlist";
    }
}
