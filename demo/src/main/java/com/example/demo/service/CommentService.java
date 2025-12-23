package com.example.demo.service;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Comment;

import java.util.List;

public interface CommentService {
    /* convert */
    // DTO => Entity
    default  Comment convertDtoToEntity(CommentDTO commentDTO){
        return Comment.builder()
                .cno(commentDTO.getCno())
                .bno(commentDTO.getBno())
                .writer(commentDTO.getWriter())
                .content(commentDTO.getContent())
                .build();
    }
    // Entity => DTO
    default CommentDTO convertEntityToDTO(Comment comment){
        return CommentDTO.builder()
                .cno(comment.getCno())
                .bno(comment.getBno())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .regDate(comment.getRegDate())
                .modDate(comment.getModDate())
                .build();
    }

    long post(CommentDTO commentDTO);

    List<CommentDTO> getList(Long bno);

    long modify(CommentDTO commentDTO);
}
