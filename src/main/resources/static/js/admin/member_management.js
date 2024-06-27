function deleteUser(uid){
    if (confirm('정말 회원 삭제하시겠습니까?')){
        fetch('/delete_user/' + uid ,{
            method : 'GET'
            , headers : {
                'Content-Type' : 'application/json'
                ,'Accept' : 'application/json'
            },
        }).then(response => response.json())
            .then(data => {

                if (data == uid) {
                    alert('회원 탈퇴가 완료되었습니다.');
                    window.location.reload();   // 페이지 새로고침
                }else {
                    alert('회원 탈퇴가 실패했습니다.');
                }
            }).catch(error => {
            console.error('회원 탈퇴 중 오류 발생', error);
            alert('회원 탈퇴 중 오류가 발생했습니다.');
        });
    } else {
        console.log('회원 탈퇴를 취소했습니다.');
    }
}
