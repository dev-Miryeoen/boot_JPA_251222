package com.example.demo.service;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    @Override
    public long post(CommentDTO commentDTO) {
        // 저장 대상은 Entity CommentDTO => Entity 변환
        return commentRepository.save(convertDtoToEntity(commentDTO)).getCno();
    }

    @Override
    public List<CommentDTO> getList(Long bno) {
        /* findBy** => ** 테이블 안에 있는 모든 칼럼
        *  select * from comment where ** = ?  */
        List<Comment> list = commentRepository.findByBno(bno);
        return list.stream().map(comment -> convertEntityToDTO(comment)).toList();
    }

    @Transactional
    @Override
    public long modify(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getCno())
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
        comment.setContent(commentDTO.getContent());
        return comment.getCno();
    }
}
