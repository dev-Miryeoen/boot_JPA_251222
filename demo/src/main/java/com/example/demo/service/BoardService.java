package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.entity.Board;
import org.springframework.data.domain.Page;

public interface BoardService {
    // 추상메서드만 가능한 인터페이스
    // default method : 인터페이스에서 규칙을 잡거나, 로직을 잡거나 할 때 사용
    // 호환성 유지

    /* BoardDTO => Board 객체로 변환
    *  BoardDTO(class) : bno, title, writer, content, readCount, cmtQty, fileQty, regDate, modDate
    *  Board(entity) : bno, title, writer, content, readCount, cmtQty, fileQty
    *  화면 => DB
    * */
    default Board convertDtoToEntity(BoardDTO boardDTO){
        return Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .writer(boardDTO.getWriter())
                .content(boardDTO.getContent())
                .readCount(boardDTO.getReadCount())
                .cmtQty(boardDTO.getCmtQty())
                .fileQty(boardDTO.getFileQty())
                .build();
    }

    /* 반대 케이스
    *  DB => 화면
    *  Board => BoardDTO
    * */
    default BoardDTO convertEntityToDTO(Board board){
        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .writer(board.getWriter())
                .content(board.getContent())
                .readCount(board.getReadCount())
                .cmtQty(board.getCmtQty())
                .fileQty(board.getFileQty())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();
    }

    Long insert(BoardDTO boardDTO);

    Page<BoardDTO> getList(int pageNo);

    BoardDTO getDetail(long bno);

    Long modify(BoardDTO boardDTO);

    void delete(long bno);
}
