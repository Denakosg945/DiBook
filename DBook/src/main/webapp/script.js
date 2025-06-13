//debounce function because checking every ms will be taxxing for the system.
export function debounce(func, delay=500){
    let timer;
    return function(...args){
        clearTimeout(timer);
        timer = setTimeout(() => {
            func.apply(this,args);
        },delay);
    }
}

export function initUsernameCheck(){
	
	//Get elements from the page using document object
	const unameInput = document.getElementById("uname");
	const submitButton = document.getElementById("submit-btn");
	const logInHeader = document.querySelector("h1");
	const pwdInput = document.getElementById("pwd");
	const existsInput = document.getElementById("exists-input");
	
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



export function initPasswordCheck() {
	const newPassword = document.getElementById("newPwd");
	const newPasswordVerify = document.getElementById("newPwdVerify");
	const forgotBtn = document.getElementById("forgot-btn");
	const unameInput = document.getElementById("resetPageUname");

	let usernameExists = false;

	async function checkResetUname(username) {
		try {
			const response = await fetch('/DBook/CheckUserServlet', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({ username: username }),
			});

			if (!response.ok) {
				throw new Error(`Error occurred: ${response.status}`);
			}

			const exists = await response.json();
			usernameExists = exists.exists;
			updateButtonState();
		} catch (error) {
			console.error("Failed to check username:", error.message);
			usernameExists = false;
			updateButtonState();
		}
	}

	function doPasswordsMatch() {
		const pwd1 = newPassword.value.trim();
		const pwd2 = newPasswordVerify.value.trim();
		return pwd1 !== "" && pwd1 === pwd2 && passwordRegExTest(pwd1);
	}

	function updateButtonState() {
		// Enable only if username exists AND passwords match
		if (usernameExists && doPasswordsMatch()) {
			forgotBtn.removeAttribute("disabled");
		} else {
			forgotBtn.setAttribute("disabled", "true");
		}
	}
	
	function passwordRegExTest(pwd){
		if(pwd.length < 5){
			return false;
		}
		
		const passwordRegex = new RegExp("^(?=.*[A-Za-z])(?=.*\\d).+$");
		if(passwordRegex.test(pwd)){
			return true;
		}
		return false;
		
	}

	const debounceUsernameCheck = debounce(() => {
		checkResetUname(unameInput.value);
	}, 500);

	const debouncePasswordCheck = debounce(() => {
		updateButtonState();
	}, 500);
	
	

	unameInput.addEventListener("input", debounceUsernameCheck);
	newPassword.addEventListener("input", debouncePasswordCheck);
	newPasswordVerify.addEventListener("input", debouncePasswordCheck);
}





