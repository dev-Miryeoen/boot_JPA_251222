package com.example.demo.handler;

import com.example.demo.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class FileRemoveHandler {
    private final String DIR = "C:\\web_0826_lsw\\_myProject\\_java\\_fileUpload\\";

    public boolean removeFile(FileDTO fileDTO) {

        // C:\web_0826_lsw\_myProject\_java\_fileUpload\2025\12\10\\uuid_fileName
        // C:\web_0826_lsw\_myProject\_java\_fileUpload\2025\12\10\\uuid_th_fileName
        // file.delete() // 파일 삭제
        // image file 썸네일도 같이 삭제
        boolean isDel = false;
        File fileDir = new File(DIR, fileDTO.getSaveDir());
        String removeFile = fileDTO.getUuid()+"_"+fileDTO.getFileName();
        File deleteFile = new File(fileDir, removeFile);

        String removeThFile = fileDTO.getUuid()+"_th_"+fileDTO.getFileName();
        File deleteThFile = new File(fileDir,removeThFile);

        try {
            // 파일이 존재하는지 확인
            if(deleteFile.exists()) {
                isDel = deleteFile.delete();
                log.info(">>> deleteFile success >> {}", deleteFile.toString());
                if(isDel && fileDTO.getFileType() == 1 && deleteThFile.exists()) {
                    isDel = deleteThFile.delete();
                    log.info(">>> deleteThFile success >> {}", deleteThFile.toString());
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.info(">>> delete file error >>");
            e.printStackTrace();
        }
        return isDel;
    }
}
