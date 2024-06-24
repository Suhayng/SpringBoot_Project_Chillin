

/** 일단은 해보고, 혹시 fetch 타이밍이 문제가 있으면 await 라도 해보기 */
let rep_boom_create = function (boom_div, rid){

    fetch('/community/getRepBoom/'+rid , {
        method : 'GET',
        headers :{
            'Accept' : 'application/json'
        }
    }).then(response =>{
        if(!response.ok){
            throw new Error('rep boom 정보 가져오기 실패')
        }else{
            return response.json();
        }
    }).then(data =>{

        let rep_boomup_li = document.createElement('li');
        let rep_boomup_img = document.createElement('img');

        if(data.status)
        rep_boomup_img.src = '/images/boomup.png'



        let rep_boomdown_li = document.createElement('li');


    }).catch(error =>{
        console.log(error + 'rep 가져오면서 에러');
    })


}









