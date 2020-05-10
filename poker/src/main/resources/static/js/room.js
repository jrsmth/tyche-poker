

// test - state change
//notification("OWEN BETS 15", "fa fa-check-circle", "good");


// janky state refresh
setTimeout(function () {
    window.location.reload(1);
}, 10000);


// populate this user's uuid
var uuid = document.getElementById("user-uuid").innerHTML;
console.log(uuid);
var uuidList = document.getElementsByClassName("uuid");
for (let uuidItem of uuidList) {
    uuidItem.value = uuid;
}


// set this user
var userCard0 = document.getElementById("user-card0").innerHTML;
var userCard0Elem = document.createElement("img");
userCard0Elem.src = "./res/PNG-cards-1.3/" + userCard0 + ".png"; userCard0Elem.className = "state-this-user-card";
document.getElementById("state-this-user-hand").appendChild(userCard0Elem);
var userCard1 = document.getElementById("user-card1").innerHTML;
var userCard1Elem = document.createElement("img");
userCard1Elem.src = "./res/PNG-cards-1.3/" + userCard1 + ".png"; userCard1Elem.className = "state-this-user-card";
document.getElementById("state-this-user-hand").appendChild(userCard1Elem);


// set other users
var othersName = document.getElementsByClassName("others-name");
var othersChips = document.getElementsByClassName("others-chips");
for(var i = 0; i < othersName.length; i++){
    // create the html for a new user and append to state-other-users-table
    var othersContainer = document.createElement("div");
    othersContainer.className = "budget_overview_container";
    othersContainer.className = "other-users";
    othersContainer.innerHTML = '<i class="fa fa-user others-icon"></i> ' + othersName[i].innerHTML + ' <br> <span class="other-users-chips fadeIn"> ' + othersChips[i].innerHTML + ' </span>';
    document.getElementById("state-other-users-table").appendChild(othersContainer);
}


// set table state
var tablePot = document.getElementById("table-pot").innerHTML; document.getElementById("state-table-info-pot").innerHTML = '<i class="fas fa-piggy-bank"></i> ' + tablePot;
var tableBet = document.getElementById("table-bet").innerHTML; document.getElementById("state-table-info-bet").innerHTML = '<i class="fas fa-dollar-sign"></i> ' + tableBet;

var tableFlop0 = document.getElementById("table-cards-flop0").innerHTML;
var tableFlop0Elem = document.createElement("img");
tableFlop0Elem.src = "./res/PNG-cards-1.3/reverse.png"; tableFlop0Elem.className = "state-table-card"; tableFlop0Elem.id="flop0";
document.getElementById("state-table-hand").appendChild(tableFlop0Elem);
if(tableFlop0 != "reverse"){ flip(tableFlop0, tableFlop0Elem.id) }

var tableFlop1 = document.getElementById("table-cards-flop1").innerHTML;
var tableFlop1Elem = document.createElement("img");
tableFlop1Elem.src = "./res/PNG-cards-1.3/reverse.png"; tableFlop1Elem.className = "state-table-card"; tableFlop1Elem.id="flop1";
document.getElementById("state-table-hand").appendChild(tableFlop1Elem);
if(tableFlop1 != "reverse"){ flip(tableFlop1, tableFlop1Elem.id) }

var tableFlop2 = document.getElementById("table-cards-flop2").innerHTML;
var tableFlop2Elem = document.createElement("img");
tableFlop2Elem.src = "./res/PNG-cards-1.3/reverse.png"; tableFlop2Elem.className = "state-table-card"; tableFlop2Elem.id="flop2";
document.getElementById("state-table-hand").appendChild(tableFlop2Elem);
if(tableFlop2 != "reverse"){ flip(tableFlop2, tableFlop2Elem.id) }

var tableTurn = document.getElementById("table-cards-turn").innerHTML;
var tableTurnElem = document.createElement("img");
tableTurnElem.src = "./res/PNG-cards-1.3/reverse.png"; tableTurnElem.className = "state-table-card"; tableTurnElem.id="turn";
document.getElementById("state-table-hand").appendChild(tableTurnElem);
if(tableTurn != "reverse"){ flip(tableTurn, tableTurnElem.id) }

var tableRiver = document.getElementById("table-cards-river").innerHTML;
var tableRiverElem = document.createElement("img");
tableRiverElem.src = "./res/PNG-cards-1.3/reverse.png"; tableRiverElem.className = "state-table-card"; tableRiverElem.id="river";
document.getElementById("state-table-hand").appendChild(tableRiverElem);
if(tableRiver != "reverse"){ flip(tableRiver, tableRiverElem.id) }


// this user's turn?
var userTurn = document.getElementById("user-turn").innerHTML;
console.log(userTurn);
if(userTurn === "true"){
    document.getElementById("this-user").className += " turn-true";
} else {
    // remove class name and assign to correct user
    document.getElementById("state-this-user-action").style.display = "none";
    var othersTurn = document.getElementsByClassName("others-turn");
    console.log(othersTurn[0].innerHTML);
    var othersUsers = document.getElementsByClassName("others-icon");
    console.log(othersUsers[0]);
    for (var i = 0; i < othersTurn.length; i++){
        if (othersTurn[i].innerHTML === "true") { console.log("hit!"); othersUsers[i].className += " turn-true"; }
    }
}


// notification
function notification(contents, icon, mood){
    var notification = document.getElementById("notification"); notification.className = "show";
    var notificationIcon = document.getElementById("notification-i"); notificationIcon.className = icon;
    var notificationDesc = document.getElementById("notification-desc"); notificationDesc.innerHTML = contents;
    switch(mood){
        case "good": notificationIcon.style.color = "#1de760"; break;
        case "bad": notificationIcon.style.color = "#d12d36"; break;
    }
    setTimeout(function(){ notification.className = notification.className.replace("show", ""); }, 5000);
}


// flip card
function flip(cardToFlip, id){
    document.getElementById(id).className += " flip-true";
    setTimeout(function(){ document.getElementById(id).src = "./res/PNG-cards-1.3/" + cardToFlip + ".png"; }, 1000);
}


// when it is not this user's, toggle disable this function - not yet bc demonstration
// make turn
function makeTurn(action){

    var userTurn = document.getElementById("user-turn").innerHTML;
    console.log(userTurn);
    if (userTurn === "false"){
        notification("NOT YOUR TURN", "fa fa-times-circle", "bad");
        return null;
    }

    if (action === "rais"){
        var betValue = document.getElementById("raise-value").value;
    } else {
        var betValue = "0";
    }

    return fetch('/turn', {
        method: 'POST',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "uuid": document.getElementById("user-uuid").innerHTML, // which user
            "action": action, // which action
            "betValue": betValue
        }),
    })
    .catch((error) =>{
        notification("INVALID TURN", "fa fa-times-circle", "bad");
    });
}

