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
})

// list 버튼 이동 함수 /board/list로 이동
// document.getElementById('listBtn').addEventListener('click',()=>{
//     window.location.href = "/board/list"
// })