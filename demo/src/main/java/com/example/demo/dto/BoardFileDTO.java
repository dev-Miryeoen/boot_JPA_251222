package com.example.demo.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardFileDTO {
    private BoardDTO boardDTO;
    private List<FileDTO> fileDTOList;
}
