let params;
let init = function (inits) {
    params = inits;
    // console.log(params);
}

window.onload = function () {

    //기존 사용자 아이디/닉네임/userid
    let user_id = params.dto_id;
    let user_nickName = params.dto_nickName;
    let user_userid = params.dto_userid;


    // console.log("user_id : "+ user_id);
    // console.log("user_nickName: "+ user_nickName);
    // console.log("useruid : "+ user_userid);


//수정 저장버튼
    let btn_modi = document.getElementById('btn_modi');
    //아이디 input
    let input_id = document.getElementById('id');
    //닉네임 input
    let input_nick = document.getElementById('nickName');

    // 비밀번호 input
    let input_pw = document.getElementById('password');
    let input_chek_pw = document.getElementById('chek_pw');

    /**저장 버튼 비활성화 함수*/
    let modi_disabled = function () {
        btn_modi.disabled = true;
        btn_modi.style.background = '#F5F5F5';
        btn_modi.style.border = '2px solid #F5F5F5';
        btn_modi.style.color = 'gray';
    }

    /**저장 버튼 활성화 함수*/
    let modi_abled = function () {
        btn_modi.disabled = false;
        btn_modi.style.removeProperty('background');
        btn_modi.style.removeProperty('border');
        btn_modi.style.removeProperty('color');
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
                if (data == 0 || idvalue == user_id) {

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

        }).catch(error => {
            console.log(error);
        })
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
                if (data == 0 || user_nickName == nickvalue) {

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

        }).catch(error => {
            console.log(error);
        })

    }

    /**초기화 버튼 클릭 시 입력창 막아둔것 풀기*/
    document.getElementById('reset').onclick = function () {
        input_nick.readOnly = false;
        input_id.readOnly = false;
        input_pw.readOnly = false;
        input_chek_pw.readOnly = false;
        input_nick.style.removeProperty('background');
        input_id.style.removeProperty('background');
        input_pw.style.removeProperty('background');
        input_chek_pw.style.removeProperty('background');
        let existingSpannick = document.getElementById('nick-check-span');
        if (existingSpannick) {
            document.getElementById("nick_check").removeChild(existingSpannick);
        }
        let existingSpanid = document.getElementById('id-check-span');
        if (existingSpanid) {
            document.getElementById("id_check").removeChild(existingSpanid);
        }
        // 기존 span 요소를 찾고, 있으면 제거
        let existingSpanpw = document.getElementById('pw-check-span');
        if (existingSpanpw) {
            document.getElementById("password_check").removeChild(existingSpanpw);
        }
    }




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

        if (input_pw.value == input_chek_pw.value) {
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

    };

    /**회원 탈퇴하기*/
    let deleteUserBtn = document.getElementById('deleteuser_btn');

    deleteUserBtn.addEventListener('click', function () {


        const userConfirmed = confirm('회원 탈퇴하시겠습니까?');

        // 사용자가 확인 버튼을 눌렀을 때
        if (userConfirmed) {

            fetch('/delete_user/' + user_userid, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                    , 'Accept': 'application/json'
                },
            })
                .then(response => response.json())
                .then(data => {

                    if (data == user_userid) {

                        alert('회원 탈퇴가 완료되었습니다.');
                        location.href = '/logout';

                    } else {
                        alert('회원 탈퇴에 실패했습니다. 다시 시도해 주세요.');
                    }
                })
                .catch(error => {
                    console.error('회원 탈퇴 중 오류 발생:', error);
                    alert('회원 탈퇴 중 오류가 발생했습니다.');
                });
        } else {
            // 사용자가 취소 버튼을 눌렀을 때
            console.log('회원 탈퇴를 취소했습니다.');
        }
    });


}//window