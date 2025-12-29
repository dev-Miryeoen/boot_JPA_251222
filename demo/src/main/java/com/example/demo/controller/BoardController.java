package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.BoardFileDTO;
import com.example.demo.dto.FileDTO;
import com.example.demo.handler.FileHandler;
import com.example.demo.handler.FileRemoveHandler;
import com.example.demo.handler.PageHandler;
import com.example.demo.service.BoardService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/*")
@Controller
public class BoardController {
    private final BoardService boardService;
    private final FileHandler fileHandler;

    @GetMapping("/register")
    public void register(){

    }

    @PostMapping("/register")
    public String register(BoardDTO boardDTO, @RequestParam(name = "files", required = false) MultipartFile[] files){
        // 파일처리
        // 저장될 파일 데이터 + 직접 폴더에 파일을 저장
        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0){
            // 핸들러 호출
            fileList = fileHandler.uploadFile(files);
        }
        log.info(">>> fileList >> {}", fileList);

        BoardFileDTO boardFileDTO = new BoardFileDTO(boardDTO, fileList);
        Long bno = boardService.insert(boardFileDTO);

//        Long bno = boardService.insert(boardDTO);
//        log.info(">>> insert id >> {}", bno);
        return "redirect:/board/list";
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
        BoardFileDTO boardFileDTO = boardService.getDetail(bno);
        model.addAttribute("boardFileDTO",boardFileDTO);
    }

    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO, @RequestParam(name = "files", required = false) MultipartFile[] files, RedirectAttributes redirectAttributes){
        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0){
            // 핸들러 호출
            fileList = fileHandler.uploadFile(files);
        }
        BoardFileDTO boardFileDTO = new BoardFileDTO(boardDTO, fileList);
        Long bno = boardService.modify(boardFileDTO);
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/detail";
    }

    @GetMapping("/remove")
    public String remove(@RequestParam("bno") long bno){
        boardService.delete(bno);
        return "redirect:/board/list";
    }

    @DeleteMapping("/delFile/{uuid}")
    public ResponseEntity<String> deleteFile(@PathVariable("uuid") String uuid){
        // 파일을 먼저 삭제하고, DB데이터 삭제
        FileDTO removeFile = boardService.getFile(uuid);
        FileRemoveHandler fileRemoveHandler = new FileRemoveHandler();
        boolean isDel = fileRemoveHandler.removeFile(removeFile);

        long bno = 0;
        if(isDel){
            bno = boardService.delFile(uuid);
        }
        return bno > 0 ? ResponseEntity.ok("1") :
                ResponseEntity.internalServerError().build();
    }
}
