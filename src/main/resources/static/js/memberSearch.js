/* 버튼 클릭 시 서밋하고 친구상태 불러오기 / 비동기로 버튼 비활성화 */
$(document).on("click", ".btn", function(){
	let button = $(this);
	let member_id = button.val();
	$.ajax({
		url		: "/friend/friendRequest",
		type	: "post",
		data	: {member_id : member_id},
		success	: function(status){
			if (status != null) {
				button.prop("disabled", true);
			}
		}
	});
});

/* 키워드 검색 글자수 3글자 미만 알림창 띄우는 메서드 (input type="text" : maxlength 밖에 없음) */
const keywordInput = document.getElementById('keywordInput');
const keywordBtn = document.getElementById('keywordBtn');
const keywordForm = document.getElementById('keywordForm');
keywordBtn.addEventListener('click', () => {
	const keyword = keywordInput.value.trim();
	if (keyword.length < 3) {
		alert('최소 3글자 이상 입력하세요.');
		return false;
	}
	keywordForm.submit();
});

/* 링크 클릭 시 직접 서밋 */
const infoA = document.querySelectorAll('.infoA');
infoA.forEach(button => {
	button.addEventListener('click', () => {
		const infoId = button.getAttribute('data-info');
		const infoForm = document.getElementById(infoId);
		infoForm.submit();
	});
});