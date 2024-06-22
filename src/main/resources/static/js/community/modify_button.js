

let board_id;
let setBoardId = function (bid){
    board_id = bid;
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

    fetch('/community/modify/'+board_id,{
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
        if (data.bid > 0) {
            location.href = '/community/' + data.bid;
        }
    }).catch(error => {
        console.log('subInsert fetch' + error);
    }).finally(() => {
        console.log('subInsert - finally')
    });

}





