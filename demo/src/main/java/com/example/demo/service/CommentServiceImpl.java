package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public long post(CommentDTO commentDTO) {
        // 저장 대상은 Entity CommentDTO => Entity 변환
        // save()
        // 해당 comment에 board 객체를 가져와서 cmtQty + 1 업데이트
        Board board = boardRepository.findById(commentDTO.getBno()).orElseThrow(()->new EntityNotFoundException());
        board.setCmtQty(board.getCmtQty()+1);

        return commentRepository.save(convertDtoToEntity(commentDTO)).getCno();
    }

    @Override
    public Page<CommentDTO> getList(Long bno, int page) {
        // select * from comment where bno = #{bno} order by bno desc limit page, 5;
        Pageable pageable = PageRequest.of(page-1, 5, Sort.by("cno").descending());

        Page<Comment> list = commentRepository.findByBno(bno, pageable);
        return list.map(this::convertEntityToDTO);
    }

//    @Override
//    public List<CommentDTO> getList(Long bno) {
//        /* findBy** => ** 테이블 안에 있는 모든 칼럼
//        *  select * from comment where ** = ?  */
//        List<Comment> list = commentRepository.findByBno(bno);
//        return list.stream().map(comment -> convertEntityToDTO(comment)).toList();
//    }

    @Transactional
    @Override
    public long modify(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getCno())
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
        comment.setContent(commentDTO.getContent());
        return comment.getCno();
    }

    @Transactional
    @Override
    public void delete(Long cno) {
        Comment comment = commentRepository.findById(cno)
                        .orElseThrow(()->new EntityNotFoundException());
        Board board = boardRepository.findById(comment.getBno())
                        .orElseThrow(()->new EntityNotFoundException());
        board.setCmtQty(board.getCmtQty()-1);
        commentRepository.deleteById(cno);
    }
}
