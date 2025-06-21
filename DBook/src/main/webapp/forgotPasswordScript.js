import { NotExists, pwdNoMatch, alreadyExists,passwordChanged,passwordChangedErr,debounce } from './script.js';


export function ForgotPasswordModule() {
  const newPassword = document.getElementById('newPwd');
  const newPasswordVerify = document.getElementById('newPwdVerify');
  const forgotBtn = document.getElementById('forgot-btn');
  const unameInput = document.getElementById('resetPageUname');
  const infoField = document.getElementById('info-field');
  const currentPwd = document.getElementById('resetPagePwd');

  let usernameExists = false;

  async function checkResetUname(username) {
    try {
      const response = await fetch('/DBook/CheckUserServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username }),
      });
      if (!response.ok) throw new Error(`Error: ${response.status}`);
      const { exists } = await response.json();
      usernameExists = exists;
    } catch (error) {
      console.error('Username check failed:', error);
      usernameExists = false;
    }
  }

  function clearMessages() {
    [NotExists, pwdNoMatch, alreadyExists].forEach(el => el.remove());
  }

  function passwordStatus() {
    const p1 = newPassword.value.trim();
    const p2 = newPasswordVerify.value.trim();
    if (!p1 && !p2) return { valid: false, reason: null };
    if (p1 !== p2) return { valid: false, reason: 'mismatch' };
    if (p1.length < 5 || !/^(?=.*[A-Za-z])(?=.*\d).+$/.test(p1)) return { valid: false, reason: 'regex' };
    if (p1 === currentPwd.value.trim()) return { valid: false, reason: 'reuse' };
    return { valid: true, reason: null };
  }

  function updateButtonState() {
    clearMessages();

    // Username feedback
    if (!usernameExists) infoField.appendChild(NotExists);

    // Password feedback
    const { valid, reason } = passwordStatus();
    if (!valid && usernameExists) {
      if (reason === 'reuse') {
        infoField.appendChild(alreadyExists);
      } else {
        infoField.appendChild(pwdNoMatch);
      }
    }

    // Button state
    if (usernameExists && valid) {
      forgotBtn.removeAttribute('disabled');
    } else {
      forgotBtn.setAttribute('disabled', 'true');
    }
  }

  // Debounced checks on user input
  const debounceUsernameCheck = debounce(async () => {
    await checkResetUname(unameInput.value.trim());
    updateButtonState();
  }, 500);

  const debounceValidation = debounce(() => {
      updateButtonState();
    }, 500);

  // Attach listeners
  unameInput.addEventListener('input', debounceUsernameCheck);
  newPassword.addEventListener('input', debounceValidation);
  newPasswordVerify.addEventListener('input', debounceValidation);
  currentPwd.addEventListener('input', debounceValidation);
  
  // Initial validation
  (async () => {
    await checkResetUname(unameInput.value.trim());
    updateButtonState();
  })();
  
  document.getElementById("submitForm").addEventListener('submit',async function (e){
	e.preventDefault();
	
	const formData = new FormData(e.target);
	
	try {
	  const response = await fetch('/DBook/ForgotPassword', {
	    method: 'POST',
	    body: formData
	  });
	  
	  if (!response.ok) throw new Error(`Error: ${response.status}`);
	  
	  const data = await response.json();
	  
	  [passwordChanged, passwordChangedErr].forEach(el => el.remove());
	  if(data.success){
		infoField.appendChild(passwordChanged);
	  }else{
		infoField.appendChild(passwordChangedErr);
	  }
	  	
	} catch (error) {
	  console.error('Could not change password: ', error);
	 
	}
	
  });
  
}
