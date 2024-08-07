console.log( "view.js() ")

// board.js 에서 view 페이지 이동 코드  <th> <a href="/board/view?bno=${ b.bno }">${ b.btitle }</a>
    // JS 코드가 HTML 를 만들어내고 사용자는 표현된 HTML 에서 a 클릭이벤트
    // <a href="/board/view?bno=3">안녕하세요</a>    ---> /board/view?bno=3

// URL 상의 쿼리스트링 매개변수를 JS에서 꺼내는방법
    // JAVA SPRING 에서 HTTP URL 상의 쿼리스트링 매개변수를 꺼내는 방법
        // @RequestParam 이용한 쿼리스트링 매개변수 매핑
    // JS 에서 HTTP URL 상의 쿼리스트링 매개변수를 꺼내는 방법
        // 1. new URL( location.href )  : 현재 JS가 포함된 URL 경로 의 정보가 담긴 객체 호출
        // 2. .searchParams;            : 현재경로상의 쿼리스트링 매개변수 속성 호출
        // 3. .get( key )               : 쿼리스트링 매개변수의 key에 해당 하는 value 호출
let bno = new URL( location.href ).searchParams.get('bno'); // 현재 URL 경로상의 bno 값 호출
console.log( bno );

// 1. 개별 게시물 출력 , 매개변수는 현재 게시물의 번호
doBoardFindBno( bno )
function doBoardFindBno( bno ){
    let board = {}
    $.ajax({ // AJAX
        async : false , method : "get" ,
        url :"/board/find/bno", data : { bno : bno } ,
        success : r => { console.log(r); board = r} // 응답 받은 데이터을 ajax 밖 변수에 대입
    }) // AJAX END
    document.querySelector('.bcName').innerHTML = `${ board.bcname }`;
    document.querySelector('.etcBox').innerHTML = `<span> 작성자 ${ board.id } </span> 
                                                    <span> 조회수 ${ board.bview } </span> 
                                                    <span> 작성일 ${ board.bdate } </span>`;
                                                    
    document.querySelector('.bTitle').innerHTML = `${ board.btitle }`;
    document.querySelector('.bContent').innerHTML = `${ board.bcontent }`;

    if( board.bfile == null ){ // - 첨부파일이 없을때
        document.querySelector('.bFile').innerHTML = '';
    }else{  // - 첨부파일이 있을때
        document.querySelector('.bFile').innerHTML = `${ board.bfile.split('_')[1] } <a href="/file/download?filename=${ board.bfile }">다운로드</a>`;
    }

    document.querySelector('.btnBox').innerHTML =
            `
            <button type="button" class="btn btn-primary" onclick="location.href='/board/update?bno=${bno}'">수정</button>
            <button type="button" class="btn btn-primary" onclick="doBoardDelete(${bno})">삭제</button>
            `;
}

// 2. 댓글 쓰기
function onReplyWrite(){ console.log( 'onReplyWrite()' );
    // 1. 입력받은 댓글내용 가져오기
    let brcontent = document.querySelector('.brcontent').value
    // 2. 객체화
    let info ={
        brindex : 0 , // 댓글분류 , 0 이면 상위댓글
        brcontent : brcontent ,
        bno : bno  // 현재 보고 있는 게시물 번호( view.js 상단에서 선언된 변수 )
    }
    $.ajax({
        async : true , method : 'post' ,
        url : "/board/reply/write" ,
        data : JSON.stringify( info ) ,  // 왜 ? JSON.stringify 사용하는지 ?
        contentType : "application/json"  , // 왜? application/json 사용하는지 ?
            // - contentType : "application/x-www-form-urlencoded" : ajax 기본값(생략시 )
            // - contentType : false  , --> multipart/form-data 첨부파일( 바이너리 )
            // - contentType : "application/json"
        success : r => { console.log(r);
            if( r == true ){ alert('댓글쓰기 성공');
                document.querySelector('.brcontent').value = ``;
            }else{ alert('댓글쓰기 실패 : 로그인후 쓰기가 가능합니다. '); }
        } // success end
    }) ; // ajax end
} // f end


onReplyView();
function onReplyView(){
        let brcontentBox = document.querySelector('.brcontentBox')
        let html = '';

        $.ajax({ // AJAX
                async : false , method : "get" ,
                url :"/board/reply/write", data : { bno : bno } ,
                success : r => {
                    console.log(r);
                        r.forEach( c => {
                                        html += `<div>${c.no}</div>
                                                <div>${c.id}</div>
                                                <div>${c.brcontent}</div>`
                        })

                } //success  end

        })

brcontentBox.innerHTML = html;

}

























