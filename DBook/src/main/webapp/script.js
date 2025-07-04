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

export const topicCreated = document.createElement("small");
topicCreated.id = `topicCreated`;
topicCreated.textContent = `The topic was successfully created!`;
topicCreated.style.color = 'lime';
topicCreated.style.display = "flex";
topicCreated.style.justifyContent = "center";  
topicCreated.style.alignItems = "center"; 

export const topicNotCreated = document.createElement("small");
topicNotCreated.id = `topicNotCreated`;
topicNotCreated.textContent = `The topic could not be created!`;
topicNotCreated.style.color = 'red';
topicNotCreated.style.display = "flex";
topicNotCreated.style.justifyContent = "center";  
topicNotCreated.style.alignItems = "center"; 

export const messageNotUploaded = document.createElement("small");
messageNotUploaded.id = `messageNotUploaded`;
messageNotUploaded.textContent = `The message could not be uploaded!`;
messageNotUploaded.style.color = 'red';
messageNotUploaded.style.display = "flex";
messageNotUploaded.style.justifyContent = "center";  
messageNotUploaded.style.alignItems = "center"; 





