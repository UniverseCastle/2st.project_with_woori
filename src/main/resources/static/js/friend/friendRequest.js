$(function() {
	$(document).ready(() => {
		const status = $("#status").val();
		if (status === null || status != 'B') {
			const thenFn = () => {
				location.href = PAGE_LIST.MAIN_PAGE;
			};
			swalCall("경고", "권한이없습니다.", "warning", thenFn);
		}
	});
});

/* 링크 클릭 시 직접 서밋 */
const infoA = document.querySelectorAll('.infoA');
infoA.forEach(button => {
	button.addEventListener('click', function () {
		const infoId = button.getAttribute('data-info');
		const infoForm = document.getElementById(infoId);
		infoForm.submit();
	});
});