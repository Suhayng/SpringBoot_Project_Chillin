window.onload = function () {
    append_reps();

}

let append_reps = function (){

    fetch('/getReps/' + board_id, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('create error')
        } else {
            return response.json()
        }
    }).then(data => {
        let data_len = data.length;
        document.getElementById('rep_count')
            .innerHTML = data_len + '개의 댓글';

        let rep_list = document.getElementById('rep_list');

        for (let i = 0; i < data_len; i++) {
            let now = data[i];

            let rep_div = document.createElement('div');

            let user_div = document.createElement('div');
            let user_text = document.createTextNode('작성자 : ' + now.id);
            user_div.appendChild(user_text);

            let content_boom = document.createElement('div');

            let content_div = document.createElement('div');
            let content_text = document.createTextNode(now.content);
            content_div.appendChild(content_text);

            let boom_div = document.createElement('div');
            let boom_temp = document.createTextNode('임시 붐따');
            boom_div.appendChild(boom_temp);

            content_boom.appendChild(content_div);
            content_boom.appendChild(boom_div);


            let complain_date = document.createElement('div');

            let complain_div = document.createElement('div');
            let complain_temp = document.createTextNode('임시 신고');
            complain_div.appendChild(complain_temp);

            let date_div = document.createElement('div');
            let date_text = document.createTextNode(now.writeDate);
            date_div.appendChild(date_text);

            complain_date.appendChild(complain_div);
            complain_date.appendChild(date_div);

            rep_div.appendChild(user_div);
            rep_div.appendChild(content_boom);
            rep_div.appendChild(complain_date);

            rep_list.appendChild(rep_div)

            /*임시 분리*/
            let hr_tag = document.createElement('hr');
            rep_list.appendChild(hr_tag);
        }
    }).catch(error => {
        console.log('subInsert fetch' + error);
    }).finally(() => {
        console.log('subInsert - finally')
    });

}

document.getElementById('rep_create').onclick = function (){
    let rep_content = document.getElementById('rep_area').value;
    let rep_data = {
        'bid' : board_id,
        'content' : rep_content
    };
    fetch('/insertRep/' + board_id, {
        method: 'POST',
        headers: {
            'Content-type' : 'application/json',
            'Accept': 'application/json'
        },
        body : JSON.stringify(rep_data)
    }).then(response => {
        if (!response.ok) {
            throw new Error('create error')
        } else {
            return response.json()
        }
    }).then(data => {

        document.getElementById('rep_list').innerHTML = '';
        document.getElementById('rep_area').value = '';
        append_reps();
        /*잘 됐으면 자연스럽게 추가하는 메서드도 넣어야지
        * 위에 있는 어마어마한 fetch를 그냥 function 으로 두고 그걸 호출하자*/

    }).catch(error => {
        console.log('subInsert fetch' + error);
    }).finally(() => {
        console.log('subInsert - finally')
    });

}