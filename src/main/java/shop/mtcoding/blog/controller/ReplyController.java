package shop.mtcoding.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.dto.BoardDetailDTO;
import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.ReplyRepository;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class ReplyController {

    @Autowired
    private HttpSession session;


    @Autowired
    private ReplyRepository replyRepository;

    @PostMapping("/reply/save/{id}")
    public void save(@PathVariable Integer id, ReplyWriteDTO replyWrireDTO, HttpServletResponse response) throws Exception {
        //comment 유효성 검사
        if (replyWrireDTO.getComment()== null || replyWrireDTO.getComment().isEmpty()) {
            response.sendRedirect("/40x");
        }
        User user = (User) session.getAttribute("sessionUser");
        replyRepository.save(replyWrireDTO, user.getId());
        response.sendRedirect("/board/"+String.valueOf(id));
    }

    @PostMapping("/reply/{replyId}/delete")
    public void delete(@PathVariable Integer replyId, BoardDetailDTO boardDetailDTO, HttpServletResponse response) throws IOException {
        replyRepository.deleteById(replyId);
        response.sendRedirect("/board/"+boardDetailDTO.getBoardId());
    }




}
