const hideBtns = document.querySelectorAll('.hideBtn');
hideBtns.forEach(button => {
	button.addEventListener('click', () => {
		const hideId = button.getAttribute('data-hide');
		const showId = button.getAttribute('data-show');
		const submitId = button.getAttribute('data-submit');
		const hiddenId = button.getAttribute('data-hidden');
		const nickId = button.getAttribute('data-nick');
		const nameT = document.getElementById(hideId);
		const nameI = document.getElementById(showId);
		const submitBtn = document.getElementById(submitId);
		const nameH = document.getElementById(hiddenId);
		const nickF = document.getElementById(nickId);
		
		hideBtns.forEach(btn => {
			if (btn !== button) {
				btn.disabled = true;
			}
		});
		
		if (nameT) {
			nameT.style.display = 'none';
		}
		if (nameI && submitBtn) {
			submitBtn.style.display = 'inline-block';
			nameI.style.display = 'inline-block';
		}
		submitBtn.addEventListener('click', () => {
			nameH.value = nameI.value;
			nickF.submit();
		});
		button.style.display = 'none';
	});
});

const infoA = document.querySelectorAll('.infoA');
infoA.forEach(button => {
	button.addEventListener('click', () => {
		const infoId = button.getAttribute('data-info');
		const infoForm = document.getElementById(infoId);
		infoForm.submit();
	});
});