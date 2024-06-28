window.onload = function () {
    //append_reps();
    board_boomup_count();
    isBookmarked();

    append_reps2()
}


let delete_rep = function (rid) {

    if (confirm('댓글을 삭제하시겠습니까?')) {

        fetch('/community/delete_rep/' + rid, {
            method: 'DELETE'
            , headers: {
                'Accept': 'application/json'
            }
        }).then(response => {
            if (!response.ok) {
                throw new Error('rep-delete Error');
            } else {
                return response.json();
            }
        }).then(data => {
            if (data === true) {
                document.getElementById('rep_list').innerHTML = '';
                document.getElementById('rep_area').value = '';
                append_reps2();
            } else {
                alert('댓글 삭제 실패 ㅠ');
            }
        }).catch(error => {
            console.log('sub delete fetch' + error);
        }).finally(() => {
            console.log('sub delete - finally')
        });
    } else {

    }


}

document.getElementById('rep_create').onclick = function () {
    let rep_content = document.getElementById('rep_area').value;
    let rep_data = {
        'bid': board_id,
        'content': rep_content
    };

    if (rep_content != '' || rep_content != null) {

        fetch('/insertRep/' + board_id, {
            method: 'POST',
            headers: {
                'Content-type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(rep_data)
        }).then(response => {
            if (!response.ok) {
                throw new Error('create error')
            } else {
                return response.json()
            }
        }).then(data => {

            if (data.uid == null || data.uid === '') {
                throw new Error('로그인은 하고 댓글 써주세요');
            }

            document.getElementById('rep_list').innerHTML = '';
            document.getElementById('rep_area').value = '';
            append_reps2();
            /*잘 됐으면 자연스럽게 추가하는 메서드도 넣어야지
            * 위에 있는 어마어마한 fetch를 그냥 function 으로 두고 그걸 호출하자*/

        }).catch(error => {
            console.log('subInsert fetch' + error);
        }).finally(() => {
            console.log('subInsert - finally')
        });
    }
}

let append_reps2 = function () {

    fetch('/getReps2/' + board_id, {
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
            rep_div.classList.add('rep_div');

            let user_div = document.createElement('div');
            let user_button = document.createElement('button');
            user_button.type = 'button';
            user_button.classList.add('user_button')

            let user_span = document.createElement('div');
            let user_text = document.createTextNode('작성자 : ' + now.nickname);
            user_span.appendChild(user_text);
            user_button.appendChild(user_span);

            let user_click_position = document.createElement('span');
            user_click_position.classList.add('user_click_position');
            user_div.appendChild(user_button);
            user_div.appendChild(user_click_position)
            if (my_id !== now.uid) {
                user_button.onclick = function () {
                    user_see(now.uid, my_id, now.nickname, user_click_position);
                }
            }

            let content_boom = document.createElement('div');
            content_boom.classList.add('content_boom');

            let content_div;
            if (!now.blind) {
                content_div = document.createElement('pre');
                content_div.classList.add('rep_content');
                let content_text = document.createTextNode(now.content);
                content_div.appendChild(content_text);
            }else{
                content_div = document.createElement('div');
                content_div.classList.add('blind_text');
                let content_text = document.createTextNode('※블라인드 처리된 댓글입니다.※');
                content_div.appendChild(content_text);
            }

            let boom_div = document.createElement('div');
            boom_div.classList.add('boom_zone');


            rep_boom_create(boom_div, now.rid, now.boomup, now.boomdown, now.status);


            /*

                        let boom_temp = document.createTextNode('임시 붐따');
                        boom_div.appendChild(boom_temp);
            */

            content_boom.appendChild(content_div);
            content_boom.appendChild(boom_div);


            let complain_date_div = document.createElement('div');
            complain_date_div.classList.add('cddiv');

            let complain_date = document.createElement('div');
            complain_date.classList.add('complain_date');

            let rep_delete = document.createElement('button');
            rep_delete.type = 'button';
            rep_delete.classList.add('rep_delete_button');
            let rd_text = document.createTextNode('댓글 삭제');
            rep_delete.appendChild(rd_text);
            rep_delete.onclick = function () {
                delete_rep(now.rid);
            }

            let complain_div = document.createElement('button');
            complain_div.type = 'button';
            complain_div.classList.add('rep_complain');
            complain_div.onclick = function () {
                rep_complain(now.rid);
            }
            let complain_img = document.createElement('img');
            complain_img.src = '/images/siren.png';
            complain_img.classList.add('rep_siren');
            let complain_temp = document.createTextNode(' 신고');
            complain_div.appendChild(complain_img);
            complain_div.appendChild(complain_temp);

            let date_div = document.createElement('div');
            date_div.classList.add('write_date');
            let raw_date = now.writeDate.toString();
            let clean_date = raw_date.slice(2, 10) + '  ' + raw_date.slice(11, 19);
            let date_text = document.createTextNode(clean_date);
            date_div.appendChild(date_text);

            complain_date.appendChild(rep_delete);
            complain_date.appendChild(complain_div);
            complain_date.appendChild(date_div);

            complain_date_div.appendChild(complain_date)

            rep_div.appendChild(user_div);
            rep_div.appendChild(content_boom);
            rep_div.appendChild(complain_date_div);

            rep_list.appendChild(rep_div)

        }
    }).catch(error => {
        console.log('getReps fetch' + error);
    }).finally(() => {
        console.log('getReps - finally')
    });

}


