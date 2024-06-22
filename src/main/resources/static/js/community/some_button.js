

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
    })

}



