package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.BoardFileDTO;
import com.example.demo.dto.FileDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.File;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.FileRepository;
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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    @Override
    public Long insert(BoardDTO boardDTO) {
        // save() : 저장
        // 저장 객체는 Entity (Board)
        // DTO => Entity 로 변환
        Board board = convertDtoToEntity(boardDTO); // 변환 후
        Long bno = boardRepository.save(board).getBno(); // 저장

        return bno;
    }

    @Transactional
    @Override
    public Long insert(BoardFileDTO boardFileDTO) {
        BoardDTO boardDTO = boardFileDTO.getBoardDTO();
        List<FileDTO> fileDTOList = boardFileDTO.getFileDTOList();
        if(fileDTOList != null){
            boardDTO.setFileQty(fileDTOList.size());
        }
        Long bno = boardRepository.save(convertDtoToEntity(boardDTO)).getBno();
        if(bno > 0 && fileDTOList != null){
            for(FileDTO fileDTO : fileDTOList){
                fileDTO.setBno(bno);
                bno = fileRepository.save(convertDtoToEntity(fileDTO)).getBno();
            }
        }
        return bno;
    }

    @Transactional
    @Override
    public long delFile(String uuid) {
        Optional<File> optionalFile = fileRepository.findById(uuid);
        if(optionalFile.isPresent()){
            long bno = optionalFile.get().getBno();
            fileRepository.deleteById(uuid);
            // 삭제한 파일 개수만큼 갯수 차감
            Board board = boardRepository.findById(bno).orElseThrow(()-> new EntityNotFoundException());
            board.setFileQty(board.getFileQty()-1);
            return bno;
        }
        return 0;
    }

    @Override
    public FileDTO getFile(String uuid) {
        Optional<File> optionalFile = fileRepository.findById(uuid);
        if(optionalFile.isPresent()){
            return convertEntityToDto(optionalFile.get());
        }
//        File file = fileRepository.findById(uuid).orElseThrow(new EntityNotFoundException());
        return null;
    }

    @Override
    public List<FileDTO> getTodayFileList(String today) {
        List<File> fileList = fileRepository.findBySaveDir(today);
        if(fileList.isEmpty() || fileList != null){
            return fileList.stream()
                    .map(this :: convertEntityToDto)
                    .toList();
        }
        return null;
    }

//    @Override
//    public BoardDTO getDetail(long bno) {
//        /* findOne => 기본키를 이용하여 원하는 객체 검색 where ~
//        *  findby칼럼명 => 원하는 칼럼명을 이용하여 검색
//        *  findbyId = findOne
//        *  Optional<T> : NullPointException이 발생하지 않도록 도와줌
//        *  Optional.isEmpty() : null 일 경우 true / false
//        *  Optional.isPresent() : 값이 있는지를 확인 true / false
//        *  Optional.get() : 객체 가져오기
//        *  */
//        Optional<Board> optional = boardRepository.findById(bno);
//        if(optional.isPresent()){
//            Board board = optional.get();
//            // readCount update method call
//            boardReadCountUpdate(board, 1);
//
//            BoardDTO boardDTO = convertEntityToDTO(board);
//            return boardDTO;
//        }
//        return null;
//    }

    @Override
    public void delete(long bno) {
        boardRepository.deleteById(bno);
    }

    @Transactional
    @Override
    public Long modify(BoardFileDTO boardFileDTO) {
        Board board = boardRepository.findById(boardFileDTO.getBoardDTO().getBno())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글 입니다,"));
        board.setTitle(boardFileDTO.getBoardDTO().getTitle());
        board.setContent(boardFileDTO.getBoardDTO().getContent());

        boardReadCountUpdate(board, -1);

        if(boardFileDTO.getFileDTOList() != null){
            // fileList가 null이 아니면 파일 갯수 저장
            board.setFileQty(boardFileDTO.getFileDTOList().size());

            for(FileDTO fileDTO : boardFileDTO.getFileDTOList()){
                fileDTO.setBno(board.getBno());
                fileRepository.save(convertDtoToEntity(fileDTO));
            }
        }
        return board.getBno();
    }
//    @Transactional
//    @Override
//    public Long modify(BoardFileDTO boardFileDTO) {
//        /* optional.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글")) */
//        BoardDTO boardDTO = boardFileDTO.getBoardDTO();
//        Long bno = boardRepository.save(convertDtoToEntity(boardDTO)).getBno();
//        List<FileDTO> fileDTOList = boardFileDTO.getFileDTOList();
//        if(bno > 0 && fileDTOList != null){
//            for(FileDTO fileDTO : fileDTOList){
//                fileDTO.setBno(bno);
//                bno = fileRepository.save(convertDtoToEntity(fileDTO)).getBno();
//            }
//        }
//        return bno;
////        Board board = boardRepository.findById(boardDTO.getBno()).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 게시글"));
////        board.setTitle(boardDTO.getTitle());
////        board.setContent(boardDTO.getContent());
////
////        boardReadCountUpdate(board, -1);
////
////        return boardDTO.getBno();
//    }
    /* save() => id가 없으면 insert / id가 있으면 update
    *  EntityNotFoundException : where 값에 존재하지 않을 경우 발생
    *  정보 유실 가능성이 커짐
    *  dirty checking 미작동 가능성이 커짐
    *
    *  변동감지(dirty checking)
    *  findById(bno) 를 통해 먼저 조회 => 영송상태를 만든 후 수정 => save()
    *  @Transactional => dirty checking만으로 업데이트가 가능 / save() 없이도 업데이트가 가능
    *
    *  dirty checking (더티 체킹) = 변동감지
    *  엔티티가 영속성 컨텍스트에 올라가 있는 상태 (=영속상태) 일 때,
    *  해당 객체의 필드가 변경되면, 트랜젝션이 종료되기 전에 JPA가
    *  변경한 부분만 자동 감지하여 update 쿼리를 실행.
    *  sasve()가 없어도 (명시적으로 호출하지 않아도) 수정된 필드를 DB에 자동 반영
    * */

//    @Override
//    public Long modify(BoardDTO boardDTO) {
//        // update 기능은 없음
//        // findById 객체를 가져와서 => 내 객체에서 변경값만 수정 => save()
//        Optional<Board> optional = boardRepository.findById(boardDTO.getBno());
//        if(optional.isPresent()){
//            Board board = optional.get();
//            board.setTitle(boardDTO.getTitle());
//            board.setContent(boardDTO.getContent());
//            boardRepository.save(board);
//            boardReadCountUpdate(board, -1);
//            return board.getBno();
//        }
//        return 0L;
//    }

    private void boardReadCountUpdate(Board board, int i){
        // readCount update
        board.setReadCount(board.getReadCount() + i);
        boardRepository.save(board);
    }


    @Override
    public Page<BoardDTO> getList(int pageNo) {
        // limit 시작번지, 개수 => 번지는 0부터 시작
        // pageNo = 1 => limit 0,10
        Pageable pageable = PageRequest.of(pageNo-1, 10, Sort.by("bno").descending());
        Page<Board> pageList = boardRepository.findAll(pageable);
        log.info(">>> pageList >> {}", pageList);
        Page<BoardDTO> boardDTOPage = pageList.map(this::convertEntityToDTO);
        return boardDTOPage;
    }

    @Transactional
    @Override
    public BoardFileDTO getDetail(long bno) {
        Optional<Board> optional = boardRepository.findById(bno);
        if(optional.isPresent()){
            Board board = optional.get();
            board.setReadCount(board.getReadCount()+1);

            BoardDTO boardDTO = convertEntityToDTO(board);

            // file 리스트 가져오기 bno에 일치하는 파일 가져오기
            List<File> fileList = fileRepository.findByBno(bno);
            List<FileDTO> fileDTOList = fileList.stream()
                    .map(this::convertEntityToDto)
                    .toList();
            BoardFileDTO boardFileDTO = new BoardFileDTO(boardDTO, fileDTOList);
            return boardFileDTO;
        }
        return null;
    }



//    @Override
//    public Page<BoardDTO> getList(int pageNo) {
//        /* DB에서 가져오는 return List<Board> => List<BoardDTO> 로 변환
//        *  findAll() => 전체 값 리턴
//        *  select * from board order by bno desc
//        *  정렬 : Sort.by(Sort.direction.DESC, "정렬기준 칼럼명")
//        * */
//        List<Board> boardList = boardRepository.findAll(
//                Sort.by(Sort.Direction.DESC,"bno"));
//
//        List<BoardDTO> boardDTOList = boardList.stream()
//                .map(board -> convertEntityToDTO(board))
//                .toList();
//
//        return boardDTOList;
//    }
}
