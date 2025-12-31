console.log("boardModify in!");

// modBtn 버튼을 클릭하면 title, content만 readOnly = false 풀기
document.getElementById('modBtn').addEventListener('click',()=>{
    document.getElementById('t').readOnly = false;
    document.getElementById('c').readOnly = false;

    // Form 태그의 submit 역할을 하는 버튼 생성
    // <button type="녀ㅠㅡㅑㅅ" class="btn btn-success" id="regBtn">submit</button>
    let regBtn = document.createElement("button"); // <button></button>
    regBtn.setAttribute('type','submit');
    regBtn.setAttribute('id', 'regBtn');
    regBtn.classList.add('btn', 'btn-success');
    regBtn.innerText = 'Submit';

    // Form의 가장 마지막 요소로 추가
    document.getElementById('modForm').appendChild(regBtn);

    // modBtn, delBtn 삭제
    document.getElementById('modBtn').remove();
    document.getElementById('delBtn').remove();
    document.getElementById('listBtn').remove();
    // 댓글라인 삭제
    document.getElementById('comment').remove();

    // file upload 버튼 설정(표시)
    document.getElementById('trigger').style.display = 'block';

    // file-x 버튼 표시
    let fileDelBtn = document.querySelectorAll('.file-x');
    console.log(fileDelBtn);
    fileDelBtn.forEach(btn => {
        btn.style.visibility = 'visible';
        let uuid = btn.dataset.uuid;
        // btn 버튼을 클릭하면 비동기로 uuid를 보내서 DB상에서 파일을 삭제
        btn.addEventListener('click', (e)=> fileDelFunc(uuid).then(result =>{
            if(result == "1"){
                e.target.closest('li').remove();
                alert("파일이 삭제되었습니다.");
            }
        }));
    });
});

async function fileDelFunc(uuid){
    try {
        const url = `/board/delFile/${uuid}`;
        const config = {
            method: 'delete',
            headers:{
                [csrfHeader] : csrfToken
            }
        };

        const resp = await fetch(url, config);
        const result = await resp.text();
        return result;
    } catch (error){
        console.log(error);
    }
}

// list 버튼 이동 함수 /board/list로 이동
// document.getElementById('listBtn').addEventListener('click',()=>{
//     window.location.href = "/board/list"
// })