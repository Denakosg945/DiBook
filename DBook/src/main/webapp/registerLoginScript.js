import {debounce} from './script.js';


export function RegisterLoginModule(){
	
	//checks cookie and redirects to the main if the user is signed in
	window.onload = async function(){
					const response = await fetch("/DBook/CheckLogin",{
						method: 'GET',
						credentials: 'include' //sends cookies
					})
					
					const data = await response.json();
					
					if(data.logged){
						window.location.href = "/DBook/main.html";
					}
				}
	
	//Adds a small element to inform the user about the successfull account creation!
	const success = document.createElement("small");
	success.id = `successCreation`;
	success.textContent = `Account successfully created! You can now log-in.`;
	success.style.color = `lime`;
	success.style.display = "flex";
	success.style.justifyContent = "center";  
	success.style.alignItems = "center"; 
	
	//Adds a small element to inform the user if something went wrong during the account creation!
	const fail = document.createElement("small");
		fail.id = `failCreation`;
		fail.textContent = `Account could not be created...Try again later!`;
		fail.style.color = `red`;
		fail.style.display = "flex";
		fail.style.justifyContent = "center";  
		fail.style.alignItems = "center"; 
		
	//Adds a small element to inform the user if something went wrong during the account creation!
	const logInError = document.createElement("small");
		logInError.id = `logInError`;
		logInError.textContent = `Could not sign in...Try again later!`;
		logInError.style.color = `red`;
		logInError.style.display = "flex";
		logInError.style.justifyContent = "center";  
		logInError.style.alignItems = "center";	
	
	//Get elements from the page using document object
	const unameInput = document.getElementById("uname");
	const submitButton = document.getElementById("submit-btn");
	const logInHeader = document.querySelector("h1");
	const pwdInput = document.getElementById("pwd");
	const existsInput = document.getElementById("exists-input");
	const infoField = document.getElementById("info-field");
	
	document.getElementById("submitForm").addEventListener("submit",async function (e){
			e.preventDefault();
			
			const formData = new FormData(e.target);
			try{		
				const response = await fetch('/DBook/regLog', {
					method: 'POST',
					body: formData
					});
					
					if (!response.ok) {
					    const message = `Error has occurred: ${response.status}`;
					    throw new Error(message);
					}
					const data = await response.json();

					
					if(data.status === "create"){	
										
						fail.remove();
						success.remove();
						if(data.Created){
							infoField.appendChild(success);
						}else{
							infoField.appendChild(fail);
							
						}
					}else if(data.status === "logged"){
						
						document.cookie = `UID=${data.userId}; path=/; max-age=${24*3600}; Secure;`;
						window.location.href = "/DBook/main.html";
						
					}else{
						logInError.remove();
						infoField.appendChild(logInError);
					}

					
				}catch (error) {
					console.error("Failed to submit form:", error.message);
				}		
	});
	
	//sends a request in the back-end to check if the username exists (sent in json form).
	async function checkUsername(username){
	    try {
	        const response = await fetch('/DBook/CheckUserServlet', {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/json',
	            },
	            body: JSON.stringify({ username: username }),
	        });

	        if (!response.ok) {
	            const message = `Error has occurred: ${response.status}`;
	            throw new Error(message);
	        }

	        const exists = await response.json();
			
			//if the account doesn't exist sets the button to create account and reverts it if the user exists
			if(!exists.exists){
				createAccountButton();
			}else{
				revertToLogIn();
			}
			
	    } catch (error) {
	        console.error("Failed to check username:", error.message);
	    }
	}
	
	//calls the debounce function which calls checkUsername every 500 ms
	const debounceUsernameCheck = debounce(() => {
	    checkUsername(unameInput.value);
	},500);

	unameInput.addEventListener("input",debounceUsernameCheck);

	//changes the buttons to create account and the header
	function createAccountButton(){
		
		logInHeader.textContent = "Create Account";
		submitButton.value = "Create Account!";
		
	}
	
	function revertToLogIn(){
		
		//reverts the buttons to log in form
		logInHeader.textContent = "Log-In Form";
		submitButton.value = "Log-In";
		
	}
	
}