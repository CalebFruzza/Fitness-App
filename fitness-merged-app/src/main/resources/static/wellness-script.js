const menuToggle = document.getElementById("menu-toggle");
const navLinks = document.querySelector(".nav-links");
const overlay = document.querySelector(".overlay");

function closeMenu() {
if (navLinks) navLinks.classList.remove("open");
if (overlay) overlay.classList.remove("show");
if (menuToggle) menuToggle.classList.remove("active");
}

function openMenu() {
if (navLinks) navLinks.classList.add("open");
if (overlay) overlay.classList.add("show");
if (menuToggle) menuToggle.classList.add("active");
}

if (menuToggle && navLinks && overlay) {
menuToggle.addEventListener("click",(e)=>{
e.stopPropagation();

if(navLinks.classList.contains("open")){
closeMenu();
}else{
openMenu();
}
});

document.addEventListener("click",(e)=>{
if(!navLinks.contains(e.target) && !menuToggle.contains(e.target)){
closeMenu();
}
});

overlay.addEventListener("click",closeMenu);

document.querySelectorAll(".nav-links a").forEach(link=>{
link.addEventListener("click",closeMenu);
});
}


/* HEADER SHRINK ON SCROLL */

window.addEventListener("scroll",()=>{

const header = document.querySelector(".header");

if(window.scrollY > 50){
header.classList.add("shrink");
}else{
header.classList.remove("shrink");
}

});


/* SCROLL REVEAL */

const observer = new IntersectionObserver(entries=>{
entries.forEach(entry=>{
if(entry.isIntersecting){
entry.target.classList.add("show");
}
});
},{threshold:0.1});

document.querySelectorAll(".section-alt,.value-box").forEach(el=>{
observer.observe(el);
});


/* SUPPLEMENTS FILTER */

const goalBtn = document.getElementById("goalBtn");
const goalSelect = document.getElementById("goalSelect");
const showAllBtn = document.getElementById("showAllBtn");

const ageBtn = document.getElementById("ageBtn");
const ageInput = document.getElementById("ageInput");
const ageMessage = document.getElementById("ageMessage");
const saveSupplementBtn = document.getElementById("saveSupplementBtn");
const loadHistoryBtn = document.getElementById("loadHistoryBtn");
const userIdInput = document.getElementById("userIdInput");
const supplementNameInput = document.getElementById("supplementNameInput");
const dosageInput = document.getElementById("dosageInput");
const dateTakenInput = document.getElementById("dateTakenInput");
const apiMessage = document.getElementById("apiMessage");
const historyList = document.getElementById("historyList");

const supplementCards = document.querySelectorAll(".value-box");
const API_BASE_URL = "";

if(goalBtn){

goalBtn.addEventListener("click",()=>{

const goal = goalSelect.value;

supplementCards.forEach(card=>{

const type = card.dataset.type;

if(goal==="" || goal===type || type==="health"){
card.style.display="block";
}else{
card.style.display="none";
}

});

});

}

if(showAllBtn){

showAllBtn.addEventListener("click",()=>{

supplementCards.forEach(card=>{
card.style.display="block";
});

});

}

if(ageBtn){

ageBtn.addEventListener("click",()=>{

const age = parseInt(ageInput.value);

if(!age){

ageMessage.textContent="Enter valid age";
ageMessage.style.color="red";

}

else if(age<16){

ageMessage.textContent="Warning: Some supplements are 16+";
ageMessage.style.color="red";

}

else{

ageMessage.textContent="Age verified.";
ageMessage.style.color="green";

}

});

}

function showApiMessage(message,color){
if(apiMessage){
apiMessage.textContent = message;
apiMessage.style.color = color;
}
}

function renderHistory(items){
if(!historyList){
return;
}

historyList.innerHTML = "";

if(!items || items.length===0){
const emptyItem = document.createElement("li");
emptyItem.textContent = "No supplement history found for this user.";
historyList.appendChild(emptyItem);
return;
}

items.forEach(item=>{
const listItem = document.createElement("li");
listItem.textContent = `${item.dateTaken} - ${item.supplementName} (${item.dosage})`;
historyList.appendChild(listItem);
});
}

async function loadSupplementHistory(){
const userId = parseInt(userIdInput.value,10);

if(!userId){
showApiMessage("Enter a valid user ID before loading history.","red");
return;
}

showApiMessage("Loading supplement history...","#333");

try{
const response = await fetch(`${API_BASE_URL}/supplements/user/${userId}/history`);
const data = await response.json();

if(!response.ok){
throw new Error(data.message || "Unable to load supplement history.");
}

renderHistory(data);
showApiMessage("Supplement history loaded successfully.","green");
}catch(error){
renderHistory([]);
showApiMessage(error.message || "Unable to connect to the backend.","red");
}
}

async function saveSupplementIntake(){
const userId = parseInt(userIdInput.value,10);
const supplementName = supplementNameInput.value.trim();
const dosage = dosageInput.value.trim();
const dateTaken = dateTakenInput.value;

if(!userId || !supplementName || !dosage || !dateTaken){
showApiMessage("Complete all supplement fields before saving.","red");
return;
}

showApiMessage("Saving supplement intake...","#333");

try{
const response = await fetch(`${API_BASE_URL}/supplements`,{
method:"POST",
headers:{
"Content-Type":"application/json"
},
body:JSON.stringify({
userId,
supplementName,
dosage,
dateTaken
})
});

const data = await response.json();

if(!response.ok){
if(data.errors){
const firstError = Object.values(data.errors)[0];
throw new Error(firstError);
}
throw new Error(data.message || "Unable to save supplement intake.");
}

showApiMessage("Supplement intake saved successfully.","green");
loadSupplementHistory();
}catch(error){
showApiMessage(error.message || "Unable to connect to the backend.","red");
}
}

if(saveSupplementBtn){
saveSupplementBtn.addEventListener("click",saveSupplementIntake);
}

if(loadHistoryBtn){
loadHistoryBtn.addEventListener("click",loadSupplementHistory);
}

