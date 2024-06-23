/*
let session_uid = '';
let setUserId = function (uid){
    session_uid = uid;
}*/

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