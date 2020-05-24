import React, { Component } from 'react';
import axios from 'axios';
import queryString from 'query-string';
import './App.css';
import 'font-awesome/css/font-awesome.min.css';
import {Helmet} from "react-helmet";



class App extends Component {

  constructor(props) {
    super(props);
    this.notification = React.createRef();
    this.notificationI = React.createRef();
    this.notificationDesc = React.createRef();
    this.raise = React.createRef();
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  state = {
    isLoading: true,
    uuid: this.getUrlParameter('uuid'),
    users: [],
    tables: [{"uuid":null,"pot":0,"flop0":"reverse","flop1":"reverse","flop2":"reverse","turn":"reverse","river":"reverse","currentBet":0}],
    thisUser: [{"card0": "reverse", "card1": "reverse", "myTurn":false}],
    betValue: 0
  };


  componentDidMount() {
    this.loadData(true);
    this.intervalId = setInterval(() => this.loadData(false), 5000);
  }

  componentWillUnmount() {
      clearInterval(this.intervalId);
  }

  loadData(first) {

      var headers = {
        "Authorization": "Basic Z29kOmNyZWF0ZWR0aGV3b3JsZGluc2V2ZW5kYXlz",
      }

      // all users 
      fetch('/users/except/'+this.state.uuid, { method: 'GET', headers: headers})
      .then((response) => response.json())
      .then((responseJson) => {
        this.setState({users: responseJson, isLoading: false});
      })
      .catch((error) => {
        console.error(error);
      });

      // table
      fetch('/tables', { method: 'GET', headers: headers})
      .then((response) => response.json())
      .then((responseJson) => { this.setState({tables: responseJson, isLoading: false}) })
      .catch((error) => {
        console.error(error);
      });

      // thisUser 
      fetch('/users/'+this.state.uuid, { method: 'GET', headers: headers})
      .then((response) => response.json())
      .then((responseJson) => {
        this.setState({thisUser: responseJson, isLoading: false});
      })
      .catch((error) => {
        console.error(error);
      });

      if(!first){
        // simulate notification
        this.showNotification("OWEN BETS 15", "fa fa-check-circle", "good");
      }

  }

  getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    let regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    let results = regex.exec(window.location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  }

  showNotification(contents, icon, mood){
    this.notification.current.className = "show"
    this.notificationI.current.className = icon
    this.notificationDesc.current.innerHTML = contents
    switch(mood){
        case "good": this.notificationI.current.style.color = "#1de760"; break;
        case "bad": this.notificationI.current.style.color = "#d12d36"; break;
    }
    setTimeout(() => {
      this.notification.current.className = ""
    }, 5000);
  }

  handleChange(event, betValue) {
    console.log("hit")
    this.setState({betValue: betValue});
    console.log("1st check:" + this.state.betValue)
    console.log(this.raise.current)
    return betValue;
  }

  handleSubmit(event, action) {
    event.preventDefault();

    let body = {
      "action": action,
      "uuid": this.state.uuid,
      "betValue": this.state.betValue
    }

    console.log("send:" + body.betValue)


    fetch('http://localhost:8080/turn', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(body),
    })
    .then((response) => console.log(response.json()))
  }


  render() {
    const {users, tables, thisUser, isLoading} = this.state;

    if (isLoading) {
      return <p> patience is a virtue, bitch - Luke 19:12 </p>;
    }

    return (

      <html id = "room">

        <head>
          <Helmet>
            <script src="https://kit.fontawesome.com/096f2be9ca.js" crossOrigin="anonymous"></script>
          </Helmet>
        </head>

        <body>

            {/* Notification */}
            <div id="notification" ref={this.notification}><div id="img" ref="img"><i id="notification-i" ref={this.notificationI}></i></div><div id="notification-desc" ref={this.notificationDesc}> </div></div>

            {/* Display Other User's State */}
            <div id="state-other-users">
                <div id="state-other-users-table">
                  {users.map(user =>
                    <div className="other-users" key={user.uuid}>
                      <i className={"fa fa-user turn-" + user.myTurn}></i> {user.name} <br></br> <span className="other-users-chips fadeIn"> {user.chips} </span>
                    </div>
                  )}
                </div>
            </div>

            {/* Display Table State  */}
              <div id="state-table">
              {tables.map(table =><div>
                  <div id="state-table-info">
                      <span id="state-table-info-pot" className="fadeInUp"> <i className="fas fa-piggy-bank"></i> {table.pot} </span> 
                      <span id="state-table-info-bet" className="fadeInUp"> <i className="fas fa-dollar-sign"></i> {table.currentBet} </span> 
                  </div>

                  <div id="state-table-hand">
                      <img id="flop0" className="card state-table-card down-true" src={"./res/PNG-cards-1.3/" + table.flop0 + ".png"}></img>
                      <img id="flop1" className="card state-table-card down-true" src={"./res/PNG-cards-1.3/" + table.flop1 + ".png"}></img>
                      <img id="flop2" className="card state-table-card down-true" src={"./res/PNG-cards-1.3/" + table.flop2 + ".png"}></img>
                      <img id="turn" className="card state-table-card down-true" src={"./res/PNG-cards-1.3/" + table.turn + ".png"}></img>
                      <img id="river" className="card state-table-card down-true flip-true" src={"./res/PNG-cards-1.3/" + table.river + ".png"}></img>
                  </div>
                </div>)}
              </div>
            

            {/* Display This User's State  */}
            <div id="state-this-user">
                <div id="state-this-user-info"> <i className={"fa fa-user turn-"+thisUser.myTurn}></i> {thisUser.name} <br></br> <span id = "state-this-user-info-chips" className="fadeIn"> {thisUser.chips} </span> </div>
                <div id="state-this-user-action" className = {"turn-" + thisUser.myTurn + "-action"}>
                    <span id="state-this-user-action-check">
                            <form className="" onSubmit={ (e) => this.handleSubmit(e, 'check')} method="post">
                                <i className="fas fa-check"></i>
                                <input type="submit" value="check"></input>
                            </form>
                    </span>
                    <span id="state-this-user-action-call">
                            <form className="" onSubmit={ (e) => this.handleSubmit(e, 'call')} method="post">
                                <i className="fas fa-arrow-right"></i>
                                <input type="submit" value="call"></input>
                            </form>
                    </span>
                    <span id="state-this-user-action-raise">
                            <form className="" onSubmit={ (e) => this.handleSubmit(e, 'raise')} method="post">
                                <i className="fas fa-arrow-up"></i>
                                <input type="submit" value="raise"></input>
                                <input type="text" value={this.state.betValue} onChange={(e) => {this.setState({betValue: e.target.value}); console.log("e: " + e.target.value)}} ref={this.raise}></input>
                            </form>
                    </span>
                    <span id="state-this-user-action-fold">
                            <form className="" onSubmit={ (e) => this.handleSubmit(e, 'fold')} method="post">
                                <i className="fas fa-arrow-down"></i>
                                <input type="submit" value="fold"></input>
                            </form>
                    </span>
                    <span id="state-this-user-action-allin">
                            <form className="" onSubmit={ (e) => this.handleSubmit(e, 'allin')} method="post">
                                <i className="fas fa-rocket"></i>
                                <input type="submit" value="allin"></input>
                            </form>
                    </span>
                  
                </div>
                <div id="state-this-user-hand">
                        <img id="card0" className="card state-this-user-card" src={"./res/PNG-cards-1.3/" + thisUser.card0 + ".png"}></img>
                        <img id="card1" className="card state-this-user-card" src={"./res/PNG-cards-1.3/" + thisUser.card1 + ".png"}></img>
                </div>
            </div>



        </body>

      </html>
      
    );
  }
}

export default App;