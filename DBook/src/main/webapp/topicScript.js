import {topicCreated, topicNotCreated} from './script.js';


export function topicScript(){
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
		
		const form = document.querySelector(".topicForm");
		const div = document.querySelector("div");
		
		document.getElementById("backBtn").addEventListener("click", () => {
				window.location.href = "/DBook";
		});
		
		form.addEventListener("submit",async function(event){
			event.preventDefault();
			
			const formData = new FormData(event.target);
			
			const values = Object.fromEntries(formData.entries());
			
			const response = await fetch("/DBook/AddTopic", {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify(values),
			});
			
			const data = await response.json();
				
			[topicCreated,topicNotCreated].forEach(el => el.remove());
			if(data.success){
				div.appendChild(topicCreated);
			}else{
				div.appendChild(topicNotCreated);
			}
			
		});
		
		
		
}