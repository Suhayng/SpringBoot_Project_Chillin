let editor;
ClassicEditor.create(document.querySelector('#editor'), {
    ckfinder: {
        uploadUrl: 'http://43.203.219.64/editor/image'
    },
}).then(newEditor => {
    editor = newEditor;
}).then(editor => {
    window.editor = editor;

}).catch(error => {
    console.error('Oops, something went wrong!');
    console.error('Please, report the following error on https://github.com/ckeditor/ckeditor5/issues with the build id and the error stack trace:');
    console.warn('Build id: g64ljk55ssvc-goqlohse75uw');
    console.error(error);
});

document.getElementById('send_editor').onclick = function () {
    let title_input = document.getElementById('board_title');
    let title = title_input.value;
    let editorData = editor.getData();

    let send_data = {
        'title': title,
        'content': editorData
    }

    fetch('/notice/create_notice', {
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
        if(data.success === false){
            alert("글 작성에 실패했습니다.");
            location.href = '/notice/create';
        }
        if (data.noticeId > 0) {
            location.href = '/notice/' + data.noticeId;
        }
    }).catch(error => {
        console.log('notice Insert fetch' + error);
    }).finally(() => {
        console.log('notice Insert - finally')
    });


}





