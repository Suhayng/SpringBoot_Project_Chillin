let board_boom_status;


let board_boomup_count = function () {

    fetch('/community/board/get_boom/' + board_id, {
        method: 'GET'
        , headers: {
            'Accept': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('boom get error');
        } else {
            return response.json();
        }
    }).then(data => {
        console.log(data);

        boom_img(data.status);

        let boomup_li = document.getElementById('board_boomup_number');
        let boomdown_li = document.getElementById('board_boomdown_number');
        boom_count_li(boomup_li, data.boomup, boomdown_li, data.boomdown);

    }).catch(error => {
        console.log(error + " boom data 받아오는 데 에러 ");
    })
}

/** 붐업의 숫자를 써주는 함수 */
let boom_count_li = function (boomup_li, boomup, boomdown_li, boomdown) {
    boomup_li.replaceChildren();
    let boomup_text = document.createTextNode(boomup);
    boomup_li.appendChild(boomup_text);
    boomdown_li.replaceChildren();
    let boomdown_text = document.createTextNode(boomdown);
    boomdown_li.appendChild(boomdown_text);
}

/*붐업*/
document.getElementById('board_boomup_button').onclick = function () {

    console.log(board_boom_status)
    fetch('/community/boomup/' + board_id + "/" + board_boom_status, {
        method: 'POST',
        headers: {
            'Accept': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('보드 붐업 실패');
        } else {
            return response.json();
        }
    }).then(data => {

        /*이때 data 가 "fail" 이면 로그인을 안했다는 뜻임*/

        console.log(data);

        if (data.status === "fail") {
            alert("boomup 실패...")
        } else {
            boom_img(data.status);

            let boomup_li = document.getElementById('board_boomup_number');
            let boomdown_li = document.getElementById('board_boomdown_number');
            boom_count_li(boomup_li, data.boomup, boomdown_li, data.boomdown);

        }
    }).catch(error => {
        console.log(error + " boomup 에러 ")
    })
}

/** 붐따 */
document.getElementById('board_boomdown_button').onclick = function () {

    fetch('/community/boomdown/' + board_id + "/" + board_boom_status, {
        method: 'POST',
        headers: {
            'Accept': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('보드 붐따 실패');
        } else {
            return response.json();
        }
    }).then(data => {

        /*이때 data 가 "fail" 이면 로그인을 안했다는 뜻임*/

        console.log(data);

        if (data.status === "fail") {
            alert("boomdown 실패...")
        } else {
            boom_img(data.status);

            let boomup_li = document.getElementById('board_boomup_number');
            let boomdown_li = document.getElementById('board_boomdown_number');
            boom_count_li(boomup_li, data.boomup, boomdown_li, data.boomdown);
        }
    }).catch(error => {
        console.log(error + " boomdown 에러 ")
    })
}


/** 붐업 붐따의 이미지를 띄우는 곳 */
let boom_img = function (status) {


    let boomup_li =
        document.querySelector('#board_boomup .some_img_li');
    boomup_li.replaceChildren();
    let boomdown_li =
        document.querySelector('#board_boomdown .some_img_li');
    boomdown_li.replaceChildren();
    let boomup_img =
        document.createElement('img');
    let boomdown_img =
        document.createElement('img');

    if (status === "no" || status === "fail") {
        boomup_img.src = "/images/boomup.png";
        boomdown_img.src = "/images/boomdown.png";
        board_boom_status = "no";
    } else if (status === "up") {
        boomup_img.src = "/images/boomup_complete.png";
        boomdown_img.src = "/images/boomdown.png";
        board_boom_status = "up";
    } else if (status === "down") {
        boomup_img.src = "/images/boomup.png";
        boomdown_img.src = "/images/boomdown_complete.png";
        board_boom_status = "down";
    }
    boomup_li.appendChild(boomup_img);
    boomdown_li.appendChild(boomdown_img);

}


let bookmark_status;

let isBookmarked = function () {

    fetch('/community/board/isBookmarked/' + board_id, {
        method: 'GET'
        , headers: {
            'Accept': 'text/plain'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('bookmark_get error');
        } else {
            return response.text();
        }
    }).then(data => {
        console.log(data);

        bookmark_img(data);

    }).catch(error => {
        console.log(error + " bookmark 받아오는 데 에러 ");
    })
}


let bookmark_img = function (status) {
    let bookmark_li = document.querySelector('#board_bookmark .some_img_li');
    bookmark_li.replaceChildren();
    let bookmark_img = document.createElement('img');
    if (status === "yes") {
        bookmark_img.src = "/images/bookmarked.png";
        bookmark_status = "yes";
    } else {
        bookmark_img.src = "/images/bookmark.png";
        bookmark_status = "no";
    }
    bookmark_li.appendChild(bookmark_img);
}


document.getElementById('board_bookmark_button').onclick = function () {
    fetch('/community/bookmark/' + board_id + '/' + bookmark_status, {
        method: 'POST',
        headers: {
            'Accept': 'text/plain'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('bookmark 과정 중 error')
        } else {
            return response.text()
        }
    }).then(data => {

        if (data === 'fail') {
            alert('북마크 실패...');
        } else {
            bookmark_img(data)
        }

    }).catch(error => {
        console.log(error + 'bookmark 하는 도중 실패');
    })
}

let modal = document.getElementById('hidden_div');

let complain_form = document.getElementById('complain_form');
let complain_input = document.getElementById('complain_id');

document.getElementById('board_complain_button').onclick = function () {
    if (isLogin) {
        complain_form.action = '/board/complain';
        complain_input.name = 'bid';
        complain_input.value = board_id;
        modal.classList.remove('hidden_modal')
    }
}

document.getElementById('bc_complain_reset').onclick = function () {
    complain_form.action = '';
    modal.classList.add('hidden_modal')
}


let rep_complain = function (rid) {
    if (isLogin) {
        complain_form.action = '/rep/complain';
        complain_input.name = 'rid';
        complain_input.value = rid;
        modal.classList.remove('hidden_modal')
    }
}

let user_see = function (uid, myid, nickname, user_span) {

    user_span.replaceChildren();

    let ucdiv = document.getElementById('user_click_div');
    if (ucdiv != null) {

        document.getElementById('user_click_div').remove();
    }
    let user_click_div = document.createElement('div');
    user_click_div.id = 'user_click_div';

    let user_click_name = document.createElement('div');
    user_click_name.id = 'user_click_name';
    let click_nickname = document.createTextNode(nickname);
    user_click_name.appendChild(click_nickname);

    let hr_tag = document.createElement('hr');

    let user_click_message = document.createElement('div');
    user_click_message.classList.add('user_click_some')
    let message_a = document.createElement('a');
    if(myid != null && myid !=='') {
        message_a.href = '/mypage/messagedetail/' + myid + '/' + uid;
    }
    let message_text = document.createTextNode('쪽지 보내기');
    message_a.appendChild(message_text);
    user_click_message.appendChild(message_a);

    let user_click_board = document.createElement('div');

    user_click_board.classList.add('user_click_some');
    let user_board_a = document.createElement('a');
    user_board_a.href = '/community/user/' + uid;
    let user_board_text = document.createTextNode('작성 글 보기');
    user_board_a.appendChild(user_board_text);
    user_click_board.appendChild(user_board_a);

    user_click_div.appendChild(user_click_name);
    user_click_div.appendChild(hr_tag);
    user_click_div.appendChild(user_click_message);
    user_click_div.appendChild(user_click_board);


    let hide_button = document.createElement('button');
    hide_button.id = 'click_hide_button';
    hide_button.type = 'button';
    let hide_text = document.createTextNode('X');
    hide_button.appendChild(hide_text);
    hide_button.onclick = function () {
        user_span.replaceChildren();
    }

    user_click_div.appendChild(hide_button);
    user_span.appendChild(user_click_div);
}

let writer_user_see = function (uid,nickname){

    console.log('이게 오긴 하네 ㅋ');
    let writer_click_position = document.getElementById('writer_click_position');

    let ucdiv = document.getElementById('user_click_div');
    if (ucdiv != null) {

        document.getElementById('user_click_div').remove();
    }
    let user_click_div = document.createElement('div');
    user_click_div.id = 'user_click_div';

    let user_click_name = document.createElement('div');
    user_click_name.id = 'user_click_name';
    let click_nickname = document.createTextNode(nickname);
    user_click_name.appendChild(click_nickname);

    let hr_tag = document.createElement('hr');

    let user_click_message = document.createElement('div');
    user_click_message.classList.add('user_click_some')
    let message_a = document.createElement('a');
    if(my_id != null && my_id !=='') {
        message_a.href = '/mypage/messagedetail/' + my_id + '/' + uid;
    }
    let message_text = document.createTextNode('쪽지 보내기');
    message_a.appendChild(message_text);
    user_click_message.appendChild(message_a);

    let user_click_board = document.createElement('div');

    user_click_board.classList.add('user_click_some');
    let user_board_a = document.createElement('a');
    user_board_a.href = '/community/user/' + uid;
    let user_board_text = document.createTextNode('작성 글 보기');
    user_board_a.appendChild(user_board_text);
    user_click_board.appendChild(user_board_a);

    user_click_div.appendChild(user_click_name);
    user_click_div.appendChild(hr_tag);
    user_click_div.appendChild(user_click_message);
    user_click_div.appendChild(user_click_board);


    let hide_button = document.createElement('button');
    hide_button.id = 'click_hide_button';
    hide_button.type = 'button';
    let hide_text = document.createTextNode('X');
    hide_button.appendChild(hide_text);
    hide_button.onclick = function () {
        writer_click_position.replaceChildren();
    }

    user_click_div.appendChild(hide_button);
    writer_click_position.appendChild(user_click_div);


}