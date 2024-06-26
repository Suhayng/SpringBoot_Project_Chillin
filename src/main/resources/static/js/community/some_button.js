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
        complain_input.name='bid';
        complain_input.value=board_id;
        modal.classList.remove('hidden_modal')
    }
}

document.getElementById('bc_complain_reset').onclick = function () {
    complain_form.action = '';
    modal.classList.add('hidden_modal')
}


let rep_complain = function (rid){
    if (isLogin) {
        complain_form.action = '/rep/complain';
        complain_input.name='rid';
        complain_input.value=rid;
        modal.classList.remove('hidden_modal')
    }
}
