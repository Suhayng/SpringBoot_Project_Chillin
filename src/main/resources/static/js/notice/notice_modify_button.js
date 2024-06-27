

let board_id;
let setBoardId = function (noticeId){
    board_id = noticeId;
}

document.getElementById('reset_button').onclick = function (){
    location.href='/notice/modify/'+board_id;
}

document.getElementById('send_editor').onclick = function (){
    let title_input = document.getElementById('board_title');
    let title = title_input.value;
    let editorData = editor.getData();

    let send_data = {
        'title': title,
        'content': editorData
    }

    console.log(title);
    console.log(editorData);

    fetch('/notice/modify/'+board_id,{
        method: 'POST',
        headers: {
            'Content-type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(send_data)
    }).then(response => {
        if (!response.ok) {
            throw new Error('create error')
        } else {
            return response.json()
        }
    }).then(data => {
        console.log(data);
        if(data.success === false) {
            alert("글 수정에 실패했습니다.");
        }
        location.href = '/notice/' + data.noticeId;

    }).catch(error => {
        console.log('notice Modify fetch' + error);
    }).finally(() => {
        console.log('notice Modify - finally')
    });

}





