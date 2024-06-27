let notice_Id = '';
const init = async function(data){
    notice_Id = data;
    console.log(notice_Id);
}
document.getElementById('modify_button').onclick = function (){
    location.href="/notice/modify/"+notice_Id;
}
document.getElementById('remove_button').onclick = function (){
    location.href="/notice/delete/"+notice_Id;
}