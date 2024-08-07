package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.BoardDto;
import web.model.dto.BoardPageDto;
import web.service.BoardService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board")
public class BoardController {
    @Autowired private BoardService boardService;
    // 1. 전체 카테고리 호출
    @GetMapping("/category")
    // public List<BoardDto> bcFindAll(){
    public List< Map<String,String> > bcFindAll( ) {
        System.out.println("BoardController.bcFindAll");
        return boardService.bcFindAll();
    }
    // 2. 글 쓰기 처리
    @PostMapping("/write")
    // { "bcno" : 1 ,  "btitle" : "안녕" ,  "bcontent" : "하하하" }
    public boolean bWrite( BoardDto boardDto) {
        System.out.println("BoardController.bWrite");
        System.out.println("boardDto = " + boardDto);
        return boardService.bWrite( boardDto );
    }
    // 3. 게시물 전체 조회 처리
    @GetMapping("/find/all")
    public  BoardPageDto bFindAll( BoardPageDto pageDto ){
            // --- 매개변수
            // 1. page : 페이징 처리 에서 사용할 현재 페이지번호
            // 2. bcno : 현재 선택된 카테고리 번호
            // 3. searchKey : 검색 조회시 사용되는 필드명
            // 4. searchKeyword : 검색 조회시 사용되는 필드의 값
        return boardService.bFindAll( pageDto );
    }

    // 4. 게시물 개별 조회 처리
    @GetMapping("/find/bno")
    public BoardDto bFindBno( int bno ){
        return boardService.bFindBno(bno);
    }

    // 5.게시물의 댓글 쓰기 처리
    // ?? 왜 post 사용하는지?  // http://localhost:8080/board/reply/write
    @PostMapping("/reply/write")
    public boolean bReplyWrite( @RequestBody Map<String , String > map ){
        // ?? 왜 @RequestBody 사용하는지 ??
        // ?? 왜 map 사용하는지?
        System.out.println("BoardController.bReplyWrite"); System.out.println("map = " + map);
        return boardService.bReplyWrite( map ); // ? 왜 service 사용하는지?
    }

    //댓글 출력
    @GetMapping("/reply/write")
    public List< Map<String,String> > bReplyView(int bno){
        return boardService.bReplyView(bno);
    }

}

















