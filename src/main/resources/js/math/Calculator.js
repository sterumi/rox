const buttons = ['CE', 'AC', 'x', '7', '8', '9', '/', '4', '5', '6',
    '-', '1', '2', '3', '+', '0', '.', '='];

var currentEntry = [], totalEntry = [];
var equals = true;

const regexOperands = /[+\-\/x=]/;

const totalArea = document.getElementById("totalArea");
const currentArea = document.getElementById("currentArea");
const numberArea = document.getElementById("numberArea");
const faceHappy = document.getElementById("face-happy");

window.onload = () => {
    makeButtons();
};

function applyClick(userInput) {
    let btn = document.getElementById("b" + userInput);

    btn.onclick = () => {
        changeDisplay(userInput);
        if (equals) {
            if (!isNaN(userInput)) currentArea.textContent = '';
            else currentArea.textContent = totalEntry;

            totalArea.textContent = '';
            currentEntry = [];
            totalEntry = [];
            equals = false;
        }
        if (currentArea.textContent.length > 17 || totalArea.textContent.length > 17) {
            alert("Number Limit Reached!");
            currentArea.textContent = "";
            totalArea.textContent = "";
            equals = true;
        } else if (!isNaN(userInput)) {
            equals = false;
            currentArea.textContent = (currentArea.textContent === "0") ? userInput : currentArea.textContent + userInput;
        } else if (isNaN(userInput)) {
            if (equals) return;
            else if (userInput === "=") {
                currentEntry = filterUserInput(userInput);
                let saveUserInput = currentArea.textContent;
                operateOnEntry(currentEntry);
                equals = true;
                totalEntry = currentEntry[0];
                currentArea.textContent = saveUserInput;
                totalArea.textContent = currentEntry;
            } else if (userInput === ".") {
                let lastEntry = filterUserInput(userInput);
                if (!lastEntry.includes(".")) currentArea.textContent = currentArea.textContent + userInput;

            } else if (userInput === "AC" || userInput === "CE")
                if (userInput === "AC") {
                    changeDisplay(userInput);
                    currentArea.textContent = "";
                    totalArea.textContent = "";
                } else if (userInput === "CE") {
                    let clearedLastEntry = filterUserInput(userInput);
                    currentArea.textContent = clearedLastEntry.join('');
                }
                else {
                    let lastEntry = filterUserInput(userInput);
                    currentArea.textContent = (regexOperands.test(lastEntry)) ? currentArea.textContent : currentArea.textContent + userInput;
                }

        }
    }
}

function operateOnEntry(userEntry) {
    let a, b, c, index;
    if (userEntry.includes("x")) {
        index = userEntry.indexOf('x');
        a = Number(userEntry[index - 1]);
        b = Number(userEntry[index + 1]);
        c = a * b;
        userEntry.splice((index - 1), 3, c);
        return operateOnEntry(userEntry);
    } else if (userEntry.includes("/")) {
        index = userEntry.indexOf('/');
        a = Number(userEntry[index - 1]);
        b = Number(userEntry[index + 1]);
        c = a / b;
        userEntry.splice((index - 1), 3, c);
        return operateOnEntry(userEntry);
    } else if (currentEntry.includes("+") || currentEntry.includes("-")) {
        index = userEntry[1];
        a = Number(userEntry[0]);
        b = Number(userEntry[2]);
        console.log("index: " + index);
        if (index === '+') {
            c = a + b;
            userEntry.splice(0, 3, c);
            return operateOnEntry(userEntry);
        } else {
            c = a - b;
            userEntry.splice(0, 3, c);
            return operateOnEntry(userEntry);
        }
    }
    return userEntry;
}

function filterUserInput(userInput) {
    let testCurrentEntry;
    if (userInput === ".") {
        testCurrentEntry = currentArea.textContent.split(regexOperands);
        return testCurrentEntry.pop();
    } else if (userInput === "=") {
        testCurrentEntry = currentArea.textContent;
        testCurrentEntry = testCurrentEntry.split(/([+\-\/x=])/g);
        return testCurrentEntry;
    } else if (userInput === "CE") {
        testCurrentEntry = currentArea.textContent.split("");
        testCurrentEntry.pop();
        return testCurrentEntry;
    } else {
        testCurrentEntry = currentArea.textContent.split('');
        return testCurrentEntry.pop();
    }
}

function changeDisplay(userInput) {
    numberArea.style.display = 'block';
    if (userInput === 'AC') {
        numberArea.style.display = 'none';
        faceHappy.style.display = "block";
    }
}

function makeButtons() {
    for (let i = 0; i < buttons.length; i++) {
        let btn = document.createElement("BUTTON");
        let t = document.createTextNode(buttons[i]);
        let container = document.getElementById('container');
        btn.id = "b" + buttons[i];
        btn.className = "button";
        btn.appendChild(t);
        container.appendChild(btn);
        applyClick(buttons[i]);
    }
}