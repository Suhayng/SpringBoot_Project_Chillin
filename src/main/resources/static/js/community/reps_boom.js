

/** 일단은 해보고, 혹시 fetch 타이밍이 문제가 있으면 await 라도 해보기 */
let rep_boom_create = function (boom_div, rid , boomup, boomdown,status){

    let boomup_button = document.createElement('button');
    boomup_button.type='button';
    boomup_button.classList.add('rep_boomup_button');
    /* 붐업버튼 클릭시 이벤트 넣어놔야함 */
    let boomdown_button = document.createElement('button');
    boomdown_button.type='button';
    boomdown_button.classList.add('rep_boomdown_button');


    boomup_button.onclick = function (){
        rep_boomup(boomup_button,boomdown_button,rid);
    }
    boomdown_button.onclick = function (){
        rep_boomdown(boomup_button,boomdown_button,rid);
    }


    let boomup_ul = document.createElement('ul');
    boomup_ul.classList.add('rep_boomup_ul')
    let boomdown_ul = document.createElement('ul');
    boomdown_ul.classList.add('rep_boomdown_ul')

    let boomup_img_li = document.createElement('li');
    boomup_img_li.classList.add('rep_boomup_img_li')
    let boomdown_img_li = document.createElement('li');
    boomdown_img_li.classList.add('rep_boomdown_img_li')

    let boomup_img = document.createElement('img');
    let boomdown_img = document.createElement('img');

    if(status === "no"){
        boomup_img.src="/images/boomup.png";
        boomdown_img.src="/images/boomdown.png";
    }
    else if(status === "up"){
        boomup_img.src="/images/boomup_complete.png";
        boomdown_img.src="/images/boomdown.png";
    }
    else if(status === "down"){
        boomup_img.src="/images/boomup.png";
        boomdown_img.src="/images/boomdown_complete.png";
    }
    boomup_img_li.appendChild(boomup_img);
    boomdown_img_li.appendChild(boomdown_img);

    let boomup_count_li = document.createElement('li');
    let boomdown_count_li = document.createElement('li');

    let boomup_count = document.createTextNode(boomup);
    let boomdown_count = document.createTextNode(boomdown);
    boomup_count_li.appendChild(boomup_count);
    boomdown_count_li.appendChild(boomdown_count);

    boomup_ul.appendChild(boomup_img_li);
    boomup_ul.appendChild(boomup_count_li);
    boomdown_ul.appendChild(boomdown_img_li);
    boomdown_ul.appendChild(boomdown_count_li);

    boomup_button.appendChild(boomup_ul);
    boomdown_button.appendChild(boomdown_ul);

    boom_div.appendChild(boomup_button);
    boom_div.appendChild(boomdown_button);

}



let rep_boomup = function (boomup_button,boomdown_button,rid){

    fetch('/community/rep_boomup/'+rid,{
        method : 'POST'
        ,headers : {
            'Accept' : 'application/json'
        }
    }).then(response =>{
        if(!response.ok){
            throw new Error('붐업 하고 받아오는 데에서 에러 : ');
        }
        return response.json();
    }).then(data =>{
        console.log(data);

        if(data.status === "fail"){
            console.log('로그인 안하고 하면 안돼 ㅠ ');
        }else{
            rep_boom_img_change(data.boomup, data.boomdown,data.status,boomup_button,boomdown_button)
        }

    }).catch(error =>{
        console.error('붐업 작업 에러 : ' + error);
    })
}
let rep_boomdown = function (boomup_button,boomdown_button,rid){

    fetch('/community/rep_boomdown/'+rid,{
        method : 'POST'
        ,headers : {
            'Accept' : 'application/json'
        }
    }).then(response =>{
        if(!response.ok){
            throw new Error('붐다운 하고 받아오는 데에서 에러 : ');
        }
        return response.json();
    }).then(data =>{
        console.log(data);

        if(data.status === "fail"){
            console.log('로그인 안하고 하면 안돼 ㅠ ');
        }else{
            rep_boom_img_change(data.boomup, data.boomdown,data.status,boomup_button,boomdown_button)
        }

    }).catch(error =>{
        console.error('붐다운 작업 에러 : ' + error);
    })
}

let rep_boom_img_change = function(boomup, boomdown, status, boomup_button, boomdown_button){

    boomup_button.replaceChildren();
    boomdown_button.replaceChildren();

    let boomup_ul = document.createElement('ul');
    boomup_ul.classList.add('rep_boomup_ul')
    let boomdown_ul = document.createElement('ul');
    boomdown_ul.classList.add('rep_boomdown_ul')

    let boomup_img_li = document.createElement('li');
    boomup_img_li.classList.add('rep_boomup_img_li')
    let boomdown_img_li = document.createElement('li');
    boomdown_img_li.classList.add('rep_boomdown_img_li')

    let boomup_img = document.createElement('img');
    let boomdown_img = document.createElement('img');

    if(status === "no"){
        boomup_img.src="/images/boomup.png";
        boomdown_img.src="/images/boomdown.png";
    }
    else if(status === "up"){
        boomup_img.src="/images/boomup_complete.png";
        boomdown_img.src="/images/boomdown.png";
    }
    else if(status === "down"){
        boomup_img.src="/images/boomup.png";
        boomdown_img.src="/images/boomdown_complete.png";
    }
    boomup_img_li.appendChild(boomup_img);
    boomdown_img_li.appendChild(boomdown_img);

    let boomup_count_li = document.createElement('li');
    let boomdown_count_li = document.createElement('li');

    let boomup_count = document.createTextNode(boomup);
    let boomdown_count = document.createTextNode(boomdown);
    boomup_count_li.appendChild(boomup_count);
    boomdown_count_li.appendChild(boomdown_count);

    boomup_ul.appendChild(boomup_img_li);
    boomup_ul.appendChild(boomup_count_li);
    boomdown_ul.appendChild(boomdown_img_li);
    boomdown_ul.appendChild(boomdown_count_li);

    boomup_button.appendChild(boomup_ul);
    boomdown_button.appendChild(boomdown_ul);


}



