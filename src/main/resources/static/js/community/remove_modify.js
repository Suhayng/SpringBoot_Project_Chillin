/*
let session_uid = '';
let setUserId = function (uid){
    session_uid = uid;
}*/

let isLogin = false;
let my_id = '';
let login = function (uid){
    isLogin = true;
    my_id = uid;
    console.log(my_id)
}

let board_id;
let setBoardId = function (bid){
    board_id = bid;
}
document.getElementById('modify_button').onclick = function (){
    location.href="/community/modify/"+board_id;
}
document.getElementById('remove_button').onclick = function (){
    location.href="/community/delete/"+board_id;
}