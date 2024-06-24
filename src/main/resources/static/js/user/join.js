window.onload = function () {
    //회원가입버튼
    let btn_join = document.getElementById('btn_join');
    //아이디 input
    let input_id = document.getElementById('id');
    //닉네임 input
    let input_nick = document.getElementById('nickName');

    /**회원가입 버튼 비활성화 함수*/
    let join_disabled = function () {
        btn_join.disabled = true;
        btn_join.style.background = '#F5F5F5';
        btn_join.style.border = '2px solid #F5F5F5';
        btn_join.style.color = 'gray';
    }

    /**회원가입 버튼 활성화 함수*/
    let join_abled = function () {
        btn_join.disabled = false;
        btn_join.style.removeProperty('background');
        btn_join.style.removeProperty('border');
        btn_join.style.removeProperty('color');
    };

    /** 이메일 형식 검증 함수*/
    function validEmail(email) {
        var emailRegex = /^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
        return emailRegex.test(email);
    }

    /**중복검사 후 입력창 막기*/
    let input_readonly = function (input) {
        input.readOnly = true; // 다른 텍스트로 입력 못하도록 입력창 비활성화
        input.style.background = '#F5F5F5';
    };

    /**확인 항목 모두 readOnly인지 확인해서 회원가입 버튼 열기*/
    let join_chek = function (){
        if (input_id.readOnly && input_nick.readOnly
            && input_pw.readOnly && input_chek_pw.readOnly) {
            join_abled();
        }
    }



    // 처음에는 회원가입 버튼 비활성화
    join_disabled();


    /**아이디 중복 체크*/
    document.getElementById('btn_chek_id').onclick = function () {
        let idvalue = input_id.value;

        // 중복확인 버튼 초기화
        // 기존 span 요소를 찾고, 있으면 제거
        let existingSpan = document.getElementById('id-check-span');
        if (existingSpan) {
            document.getElementById("id_check").removeChild(existingSpan);
        }

        //이메일 중복 확인
        fetch("/id_check", {
            method: "POST"
            , headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({id: idvalue})
        }).then(response => {
            if (!response.ok)
                throw new Error('chek_id noooo response');
            return response.json();
        }).then((data) => {

            // 안내 메시지용 span 요소를 생성하고 ID 부여
            let checkspan = document.createElement('span');
            checkspan.id = 'id-check-span';
            // 안내메시지 텍스트
            let chektext;

            if (idvalue == '' || idvalue == null || !validEmail(idvalue)) {
                //입력값이 공백이거나 메일 형식이 아님

                chektext = document.createTextNode('이메일을 입력해주세요.');
                checkspan.appendChild(chektext);
                checkspan.style.color = "red";

            } else { //입력값은 정상
                if (data == 0) {

                    chektext = document.createTextNode('사용 가능합니다.');
                    checkspan.appendChild(chektext);
                    checkspan.style.color = "green";

                    //입력창 막기
                    input_readonly(input_id);

                } else {  // 중복되는 이메일

                    chektext = document.createTextNode('이미 존재하는 이메일입니다.');
                    checkspan.appendChild(chektext);
                    checkspan.style.color = "red";
                }
            }
            document.getElementById("id_check").appendChild(checkspan);

            //회원가입 가능한 상태면 가입버튼 활성화
            join_chek();
        }).catch(error => {
            console.log(error);
        })

        join_chek();
    }

    /**닉네임 중복 체크*/
    document.getElementById('btn_chek_nick').onclick = function () {
        nickvalue = input_nick.value;

        // 중복확인 버튼 초기화
        // 기존 span 요소를 찾고, 있으면 제거
        let existingSpan = document.getElementById('nick-check-span');
        if (existingSpan) {
            document.getElementById("nick_check").removeChild(existingSpan);
        }

        //닉네임 중복 확인
        fetch("/nick_check", {
            method: "POST"
            , headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({nickName: nickvalue})
        }).then(response => {
            if (!response.ok)
                throw new Error('nick_check noooo response');
            return response.json();
        }).then((data) => {

            // 안내 메시지용 span 요소를 생성하고 ID 부여
            let checkspan = document.createElement('span');
            checkspan.id = 'nick-check-span';
            // 안내메시지 텍스트
            let chektext;

            if (nickvalue == '' || nickvalue == null) {
                //입력값이 공백

                chektext = document.createTextNode('닉네임을 입력해주세요.');
                checkspan.appendChild(chektext);
                checkspan.style.color = "red";

            } else { //입력값은 정상
                if (data == 0) {

                    chektext = document.createTextNode('사용 가능합니다.');
                    checkspan.appendChild(chektext);
                    checkspan.style.color = "green";

                    //입력창 막기
                    input_readonly(input_nick);

                } else {  // 중복되는 닉네임

                    chektext = document.createTextNode('이미 존재하는 닉네임입니다.');
                    checkspan.appendChild(chektext);
                    checkspan.style.color = "red";
                }
            }
            document.getElementById("nick_check").appendChild(checkspan);

            //회원가입 가능한 상태면 가입버튼 활성화
            join_chek();
        }).catch(error => {
            console.log(error);
        })

    }

    /**초기화 버튼 클릭 시 입력창 막아둔것 풀기*/
    document.getElementById('reset').onclick = function () {
        join_disabled();
        input_nick.readOnly = false;
        input_id.readOnly = false;
        input_pw.readOnly = false;
        input_chek_pw.readOnly = false;
        input_nick.style.removeProperty('background');
        input_id.style.removeProperty('background');
        input_pw.style.removeProperty('background');
        input_chek_pw.style.removeProperty('background');
    }


    // 비밀번호 값 가져오기
    let input_pw = document.getElementById('password');
    let input_chek_pw = document.getElementById('chek_pw');

    /**비밀번호 확인하기*/
    input_chek_pw.onchange = function () {

        // 기존 span 요소를 찾고, 있으면 제거
        let existingSpan = document.getElementById('pw-check-span');
        if (existingSpan) {
            document.getElementById("password_check").removeChild(existingSpan);
        }

        // 안내 메시지용 span 요소를 생성하고 ID 부여
        let checkspan = document.createElement('span');
        checkspan.id = 'pw-check-span';
        // 안내메시지 텍스트
        let chektext;

        if(input_pw.value == input_chek_pw.value){
            chektext = document.createTextNode('비밀번호가 일치합니다.');
            checkspan.appendChild(chektext);
            checkspan.style.color = "green";
            input_readonly(input_pw);
            input_readonly(input_chek_pw);

        } else {
            chektext = document.createTextNode('비밀번호가 일치하지 않습니다.');
            checkspan.appendChild(chektext);
            checkspan.style.color = "red";
            join_disabled();
        }
        document.getElementById("password_check").appendChild(checkspan);

        //회원가입 가능한 상태면 가입버튼 활성화
        join_chek();

    };

}