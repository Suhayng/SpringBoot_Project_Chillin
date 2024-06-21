

document.getElementById('send_editor').onclick = function (){
    let title_input = document.getElementById('board_title');
    let title = title_input.value;
    let editorData = editor.getData();

    let send_data = {
        'title' : title,
        'content' : editorData
    }

    fetch('/board/create_board',{
        method:'POST',
        headers : {
            'Content-type' : 'application/json;charset=utf-8',
            'Accept' : 'application/json'
        },
        body : JSON.stringify(send_data)
    }).then(response =>{
        if(!response.ok){
            throw new Error('create error')
        }else{
            return response.json()
        }
    }).then(data =>{
        console.log(data);
        if(data.success){
            location.href = '/board/community_board'
        }
    }).catch(error =>{
        console.log('subInsert fetch'+error);
    }).finally(()=>{
        console.log('subInsert - finally')
    });


}





