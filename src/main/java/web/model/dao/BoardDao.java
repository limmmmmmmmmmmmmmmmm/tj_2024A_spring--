package web.model.dao;

import org.springframework.stereotype.Component;
import web.model.dto.BoardDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BoardDao extends Dao {
    // 1. 전체 카테고리 호출
    public List< Map<String,String> > bcFindAll( ) {
    // public List< BoardDto > bcFindAll( ) {
        System.out.println("BoardDao.bcFindAll");
        // - list 컬렉션 선언 , map컬렉션(객체) 을 여러개 저장하기 위해 list 선언
        List< Map<String, String> >  list = new ArrayList<>();
        try{ String sql = "select * from bcategory";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while( rs.next() ){
                // ==================== 1. Map 활용 ============= //
                // - map 컬렉션/객체 선언
                Map<String,String> map = new HashMap<>();
                // - map 컬렉션/객체 엔트리 2개 추가 , 카테고리번호 , 카테고리이름
                map.put( "bcno" ,  String.valueOf( rs.getInt( "bcno" )  ) );
                map.put( "bcname" ,  String.valueOf( rs.getString( "bcname" )  ) );
                // - map 컬렉션/객체를 리스트/객체에 담기
                list.add( map );
                // ==================== 2. Dto 활용 ============= //
                /* BoardDto boardDto = new BoardDto();
                boardDto.setBcno( rs.getInt( "bcno" ) );
                boardDto.setBcname( rs.getString( "bcname" ) );
                list.add( boardDto ); */
            }
        }catch (Exception e ){ System.out.println(e); }
        return list; // 리스트 반환
    }
    // 2.
    public boolean bWrite( BoardDto boardDto) {
        System.out.println("BoardDao.bWrite");
        System.out.println("boardDto = " + boardDto);
        try {
            String sql ="insert into board( bcno , btitle , bcontent , no , bfile ) " +
                    " values( ? , ? , ? , ? , ? )";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong( 1, boardDto.getBcno() );
            ps.setString( 2 , boardDto.getBtitle() );
            ps.setString( 3 , boardDto.getBcontent() );
            ps.setLong( 4 ,  boardDto.getNo() );
            ps.setString( 5 , boardDto.getBfile() );
            int count = ps.executeUpdate();
            if( count == 1 )return true;
        }catch (Exception e ){   System.out.println(e); }
        return false;
    }

    // 3-2. 전체 게시물수 반환 처리 , 조건추가1) 카테고리
    public int getTotalBoardSize(
            int bcno ,
            String searchKey , String searchKeyword ){

        try{ String sql ="select count(*) as 총게시물수 " +
                " from board inner join member " +
                " on board.no = member.no ";

            // 카테고리가 존재하면 , 0 이면 : 카테고리가 없다는 의미 , 1 이상 : 카테고리의 pk번호
            if( bcno >= 1 ){ sql += " where bcno = "+bcno; } // 1. 전체보기 : select count(*) as 총게시물수 from board  // 2. 카테고리 보기 : select count(*) as 총게시물수 from board where bcno = 숫자
            // 검색이 존재 했을때 , keyword가 존재하면
            if( searchKeyword.isEmpty() ){} // 문자열이 비어 있으면 , 검색이 없다라는 의미의 뜻 으로 활용
            else{  // 비어있지 않으면 , 검색이 있다라는 의미의 뜻 으로 활용
                // - 카테고리가 있을때는 and 추가
                if( bcno >= 1 ) { sql += " and "; }
                // - 카테고리가 없을때[전체보기]는 where 추가
                else { sql += " where "; }
                // 검색 sql
                sql += searchKey +" like '%"+searchKeyword+"%' ";
            }

            System.out.println( " sql : " + sql );

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if( rs.next() ){
                return rs.getInt( 1 ); // "총게시물수"
            }
        }catch (Exception e ){ System.out.println("e = " + e); }
        return 0;
    }

    // 3. 게시물 전체 조회 처리
    public List<BoardDto> bFindAll(
            int startRow , int pageBoardSize ,
            int bcno ,
            String searchKey , String searchKeyword ){  System.out.println("BoardDao.bFindAll");

        List<BoardDto> list = new ArrayList<>();
        try{
            String sql = "select * " +              // 1. 조회
                " from board inner join member " +  // 2. 조인 테이블
                " on board.no = member.no ";        // 3. 조인 조건
            // 4. 일반 조건1
                // - 전체보기 이면 where절 생략 , bcno = 0 이면
                // - 카테고리별 보기 이면 where 절 추가 , bcno >= 1 이상
            if( bcno >= 1 ){ sql += " where bcno = " + bcno ; }

            // 4. 일반 조건2
            if( searchKeyword.isEmpty() ){ }
            else{
                if( bcno >=1 ){ sql += " and "; }
                else{ sql += " where "; }
                sql += searchKey + " like '%"+searchKeyword+"%'";
            }

            // 5. 정렬 조건 , 내림차순
            sql += " order by board.bno desc ";
            // 6. 레코드 제한 , 페이징처리
            sql += " limit ? , ? ";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt( 1 , startRow );
            ps.setInt( 2 , pageBoardSize );
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ){
                // 레코드 를 하나씩 조회해서 Dto vs Map 컬렉션
                BoardDto boardDto = BoardDto.builder()
                        .bno( rs.getInt("bno") )
                        .btitle( rs.getString("btitle") )
                        .id( rs.getString("id") )
                        .bdate( rs.getString("bdate") )
                        .bview( rs.getInt("bview") )
                        .build();
                list.add( boardDto );
            }
        }catch (Exception e ){  System.out.println(e); }
        return list;
    }

    // 4. 게시물 개별 조회 처리
    public BoardDto bFindBno( int bno ){ System.out.println("BoardDao.bFindBno");System.out.println("bno = " + bno);
        try{
            String sql = "select bc.bcno , bcname, " +
                    " bno , btitle  , bcontent , " +
                    "        id , bdate , bview , bfile " +
                    " from board b " +
                    " inner join member m " +
                    "            inner join bcategory bc " +
                    " on b.no = m.no and b.bcno = bc.bcno " +
                    " where b.bno = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt( 1 , bno );
            ResultSet rs = ps.executeQuery();
            if( rs.next() ){
                // 레코드 를 하나씩 조회해서 Dto vs Map 컬렉션
                BoardDto boardDto = BoardDto.builder()
                        .bcno( rs.getInt("bcno") )
                        .bno ( rs.getInt("bno"))
                        .bcname( rs.getString("bcname"))
                        .btitle( rs.getString("btitle"))
                        .bcontent( rs.getString("bcontent"))
                        .id( rs.getString("id"))
                        .bdate( rs.getString("bdate"))
                        .bview( rs.getInt("bview"))
                        .bfile( rs.getString("bfile"))
                        .build();
                return boardDto;
            }
        }catch (Exception e ){  System.out.println(e); }
        return null;
    }

    // 5. 조회수 증가 처리
    public boolean bViewIncrease( int bno ){
        try{
            String sql ="update board set bview = bview + 1 where bno =? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt( 1 , bno );
            int count = ps.executeUpdate();
            if( count == 1 ) return true;
        }catch (Exception e ){  System.out.println(e); }
        return false;
    } // 5 f end

    // 5.게시물의 댓글 쓰기 처리
    public boolean bReplyWrite( Map<String , String > map ){ System.out.println("BoardDao.bReplyWrite"); System.out.println("map = " + map);
        // brindex , brcontent , no , bno 왜?? 4가지를 저장하는지?
        try{String sql = "insert into breply( brindex , brcontent , no , bno ) " +
                " values(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement( sql );
            ps.setInt( 1 , Integer.parseInt( map.get("brindex") ) );
            // 왜? Integer.parseInt 하는지?
            ps.setString( 2 , map.get("brcontent") );
            ps.setInt( 3 , Integer.parseInt( map.get("no") ) );
            ps.setInt( 4 , Integer.parseInt( map.get("bno") ) );
            int count = ps.executeUpdate();
            if( count == 1 ) return true;  // 왜? if( count == 1 ) 하는지?
        }catch (Exception e ) { System.out.println(e); }
        return false; // 왜?? true/false 사용하는지?
    }


    //댓글 출력
    public List< Map<String,String> > bReplyView(int bno){
        try {
            List< Map<String,String>> list = new ArrayList<>();
            String sql = "select * from breply inner join member on breply.no = member.no where bno = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,bno);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                // 레코드가 여러개 라서 while
                Map<String,String>map = new HashMap<>();
                map.put("no",String.valueOf(rs.getInt("no")));
                map.put("id",rs.getString("id"));
                map.put("brcontent",rs.getString("brcontent"));
                list.add( map );
            }
            return list;
        }catch (Exception e){
            System.out.println("e = " + e);
        }
        return null;
    }
} // class end





























