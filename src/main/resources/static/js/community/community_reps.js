window.onload = function () {
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
        console.log(data)
        let data_len = data.length;
        let rep_list = document.getElementById('rep_list');

        for (let i = 0; i < data_len; i++) {
            let now = data[i];
            let rep_div = document.createElement('div');

            let user_div = document.createElement('div');
            let user_text = document.createTextNode('작성자 : ' + data.id);
            user_div.appendChild(user_text);

            let content_boom = document.createElement('div');

            let content_div = document.createTextNode('div');
            let content_text = document.createTextNode(data.content);
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
            let date_text = document.createTextNode(data.writeDate);
            date_div.appendChild(date_text);

            rep_div.appendChild(user_div);
            rep_div.appendChild(content_boom);
            rep_div.appendChild(complain_date);

            rep_list.appendChild(rep_div)

        }

    }).catch(error => {
        console.log('subInsert fetch' + error);
    }).finally(() => {
        console.log('subInsert - finally')
    });


}

