import {messageNotUploaded} from './script.js';

export function topicPageFunctionality(){
	const descriptionDisplay = document.getElementById('topicDescription');
	const topic = document.getElementById("topic");
	const form = document.getElementById("messageForm");
	const messageDiv = document.getElementById("messageDiv");
	
	function updateDescription(){
		const selectedTopic = 	topic.options[topic.selectedIndex];
		const description = selectedTopic.getAttribute("data-description") || "";
		descriptionDisplay.textContent = description;
	}
	
	//Initial run
	updateDescription();
	
	document.getElementById("backBtn").addEventListener("click", () => {
			window.location.href = "/DBook";
	});
	
	topic.addEventListener("change",updateDescription);
	
	form.addEventListener("submit",async function (event){
		event.preventDefault();
		
		const formData = new FormData(event.target);

		const values = Object.fromEntries(formData.entries());
		
		// Extract 'uid' from cookies
		const cookies = document.cookie.split('; ');
		let userId = undefined;

		for (let cookie of cookies) {
		    if (cookie.startsWith('UID=')) {
		        userId = cookie.split('=')[1];
		        break;
		    }
		}
		
		values.topicId = new URLSearchParams(window.location.search).get('topicId');
		values.UID = userId; 

		
		const response = await fetch("/DBook/UploadMessage",{
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(values), 
		});
			
		const data = await response.json();
		const currentMessages = document.createElement("small");;
		
		messageNotUploaded.remove();
		if(data.success && data.messages > 0){
			window.location.href = `/DBook/TopicPage.jsp?topicId=${values.topicId}&messageUploaded=true&messageCount=${data.messages}`;
		}else{
			messageDiv.appendChild(messageNotUploaded);
		}
	})
}