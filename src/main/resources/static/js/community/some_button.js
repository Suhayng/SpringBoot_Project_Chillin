
let board_boom_status;

/** 아직 미완성임. 이거 하려면 그 숫자 받아와야함 */
let board_boomup_count = function(){

    fetch('/community/board/get_boom/'+board_id,{
        method: 'GET'
        ,headers :{
            'Accept' : 'application/json'
        }
    }).then(response=>{
        if(!response.ok){
            throw new Error('boom get error');
        }else{
            return response.json();
        }
    }).then(data=>{
        console.log(data);
        /* 아직 미완성임. 이거 하려면 그 숫자 받아와야함 */
        boom_img(data.you);
    }).catch(error=>{
        console.log(error + " boom data 받아오는 데 에러 ");
    })
}

/*붐업*/
document.getElementById('board_boomup_button').onclick = function (){

    fetch('/community/boomup/'+board_id+"/"+board_boom_status,{
        method:'POST',
        headers : {
            'Accept' : 'text/plain'
        }
    }).then(response =>{
        if(!response.ok){
            throw new Error('보드 붐업 실패');
        }else{
            return response.text();
        }
    }).then(data =>{

        /*이때 data 가 "fail" 이면 로그인을 안했다는 뜻임*/

        console.log(data);

        if(data === "fail"){
            alert("boomup 실패...")
        }else{
            boom_img(data);
        }
    }).catch(error =>{
        console.log(error + " boomup 에러 ")
    })
}

/** 붐따 */
document.getElementById('board_boomdown_button').onclick = function (){

    fetch('/community/boomdown/'+board_id+"/"+board_boom_status,{
        method:'POST',
        headers : {
            'Accept' : 'text/plain'
        }
    }).then(response =>{
        if(!response.ok){
            throw new Error('보드 붐따 실패');
        }else{
            return response.text();
        }
    }).then(data =>{

        /*이때 data 가 "fail" 이면 로그인을 안했다는 뜻임*/

        console.log(data);

        if(data === "fail"){
            alert("boomdown 실패...")
        }else{
            boom_img(data);
        }
    }).catch(error =>{
        console.log(error + " boomdown 에러 ")
    })
}




/** 붐업 붐따의 이미지를 띄우는 곳 */
let boom_img = function(status){

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

    if(status === "no"){
        boomup_img.src="/images/boomup.png";
        boomdown_img.src="/images/boomdown.png";
        board_boom_status = "no";
    }
    else if(status === "up"){
        boomup_img.src="/images/boomup_complete.png";
        boomdown_img.src="/images/boomdown.png";
        board_boom_status = "up";
    }
    else if(status === "down"){
        boomup_img.src="/images/boomup.png";
        boomdown_img.src="/images/boomdown_complete.png";
        board_boom_status = "down";
    }
    boomup_li.appendChild(boomup_img);
    boomdown_li.appendChild(boomdown_img);

}
