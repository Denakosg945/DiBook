//Adds a not exists small element for the username check
export const NotExists = document.createElement("small");
NotExists.id = `NotExists`;
NotExists.textContent = `This username does not exist!`;
NotExists.style.color = 'red';
NotExists.style.display = "flex";
NotExists.style.justifyContent = "center";  
NotExists.style.alignItems = "center"; 

//Adds a passwords mismatch small element for the password check
export const pwdNoMatch = document.createElement("small");
pwdNoMatch.id = `pwdNoMatch`;
pwdNoMatch.textContent = `The passwords do not match!`;
pwdNoMatch.style.color = `red`;
pwdNoMatch.style.display = "flex";
pwdNoMatch.style.justifyContent = "center";  
pwdNoMatch.style.alignItems = "center"; 


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

//Adds a old and new passwords match to inform the user to change the new password
export const alreadyExists = document.createElement("small");
alreadyExists.id = `alreadyExists`;
alreadyExists.textContent = `This password is the current password!`;
alreadyExists.style.color = 'red';
alreadyExists.style.display = "flex";
alreadyExists.style.justifyContent = "center";  
alreadyExists.style.alignItems = "center"; 

export const passwordChanged = document.createElement("small");
passwordChanged.id = `passwordChanged`;
passwordChanged.textContent = `The password was successfully changed!`;
passwordChanged.style.color = 'lime';
passwordChanged.style.display = "flex";
passwordChanged.style.justifyContent = "center";  
passwordChanged.style.alignItems = "center"; 

export const passwordChangedErr = document.createElement("small");
passwordChangedErr.id = `passwordChangedErr`;
passwordChangedErr.textContent = `The password could not be changed...Check your submitted info and try again!`;
passwordChangedErr.style.color = 'red';
passwordChangedErr.style.display = "flex";
passwordChangedErr.style.justifyContent = "center";  
passwordChangedErr.style.alignItems = "center"; 










