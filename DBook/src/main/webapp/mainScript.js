
export function mainFunctionality() {
	
	window.onload = async function(){
					const response = await fetch("/DBook/CheckLogin",{
						method: 'GET',
						credentials: 'include' //sends cookies
					})
					
					const data = await response.json();
					
					if(!data.logged){
						window.location.href = "/DBook";
					}
				}
	
	const dropdown = document.getElementById("settingsDropdown");
	const btn = document.getElementById("settingsBtn");
	const infoBtn = document.getElementById("infoBtn");
	const signOutBtn = document.getElementById("signOutBtn");
	const createTopicBtn = document.getElementById("topicBtn");
	const uploadMessageBtn = document.getElementById("messageBtn");

	btn.addEventListener("click", function(event) {
		event.stopPropagation(); // Prevent window click from firing
		dropdown.classList.toggle("show");
	});

	window.addEventListener("click", function(event) {
		if (!event.target.matches('.dropdown-btn')) {
			if (dropdown.classList.contains("show")) {
				dropdown.classList.remove("show");
			}
		}
	});

	infoBtn.addEventListener("click", function() {
		window.location.href = "/DBook/UserInfo.jsp";
	});

	signOutBtn.addEventListener("click", function() {
		document.cookie = "UID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; Secure;";
		window.location.href = "/DBook";
	});
	
	createTopicBtn.addEventListener("click", function() {
		window.location.href = "/DBook/createTopic.html";
	});
	
	uploadMessageBtn.addEventListener("click", function() {
		window.location.href = "/DBook/TopicPage.jsp";
	})
}