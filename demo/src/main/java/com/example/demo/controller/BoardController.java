package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.handler.PageHandler;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/*")
@Controller
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/register")
    public void register(){

    }

    @PostMapping("/register")
    public String register(BoardDTO boardDTO){
        Long bno = boardService.insert(boardDTO);
        log.info(">>> insert id >> {}", bno);
        return "redirect:/";
    }

//    @GetMapping("/list")
//    public void list(Model model){
////        페이징 없는 리스트
//        List<BoardDTO> list = boardService.getList();
//        model.addAttribute("list", list);
//    }

    @GetMapping("/list")
    public void list(Model model,
                     @RequestParam(name="pageNo", defaultValue = "1", required = false) int pageNo){
        // select * from board order by bno desc limit 0, 10;
        Page<BoardDTO> list = boardService.getList(pageNo);
        log.info(">>> getTotalElements >> {}", list.getTotalElements()); // 전체 게시글 수
        log.info(">>> getTotalPages >> {}", list.getTotalPages()); // 전체 페이지 수
        log.info(">>> getPageable >> {}", list.getPageable());
        log.info(">>> hasNext >> {}", list.hasNext()); // 다음 여부
        log.info(">>> hasPrevious >> {}", list.hasPrevious()); // 이전 여부

        PageHandler<BoardDTO> pageHandler = new PageHandler<>(list, pageNo);

        model.addAttribute("ph", pageHandler);
    }

    @GetMapping("/detail")
    public void detail(@RequestParam("bno") long bno, Model model){
        BoardDTO boardDTO = boardService.getDetail(bno);
        model.addAttribute("board",boardDTO);
    }

    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO, RedirectAttributes redirectAttributes){
        Long bno = boardService.modify(boardDTO);
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/detail";
    }

    @GetMapping("/remove")
    public String remove(@RequestParam("bno") long bno){
        boardService.delete(bno);
        return "redirect:/board/list";
    }
}
