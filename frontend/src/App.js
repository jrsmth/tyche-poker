import React, { Component } from 'react';
import queryString from 'query-string';
import './App.css';
import 'font-awesome/css/font-awesome.min.css';
import {Helmet} from "react-helmet";



class App extends Component {

  state = {
    isLoading: true,
    uuid: this.getUrlParameter('uuid'),
    users: [],
    tables: [{"uuid":null,"pot":0,"flop0":"reverse","flop1":"reverse","flop2":"reverse","turn":"reverse","river":"reverse","currentBet":0}],
    thisUser: []
  };

  componentDidMount() {
    this.loadData();
    this.intervalId = setInterval(() => this.loadData(), 5000);
  }

  componentWillUnmount() {
      clearInterval(this.intervalId);
  }

  loadData() {

      // all users 
      fetch('/users/except/'+this.state.uuid)
      .then((response) => response.json())
      .then((responseJson) => {
        this.setState({users: responseJson, isLoading: false});
      })
      .catch((error) => {
        console.error(error);
      });

      // table
      fetch('/tables')
      .then((response) => response.json())
      .then((responseJson) => { this.setState({tables: responseJson, isLoading: false}) })
      .catch((error) => {
        console.error(error);
      });

      // thisUser 
      fetch('/users/'+this.state.uuid)
      .then((response) => response.json())
      .then((responseJson) => {
        this.setState({thisUser: responseJson, isLoading: false});
      })
      .catch((error) => {
        console.error(error);
      });
  }

  getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    let regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    let results = regex.exec(window.location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  }

  render() {
    const {users, tables, thisUser, isLoading} = this.state;

    if (isLoading) {
      return <p>loading</p>;
    }

    return (

      <html id = "room">

        <head>
          <Helmet>
            <script src="https://kit.fontawesome.com/096f2be9ca.js" crossOrigin="anonymous"></script>
          </Helmet>
        </head>

        <body>

            {/* Display Other User's State */}
            <div id="state-other-users">
                <div id="state-other-users-table">
                  {users.map(user =>
                    <div className="other-users" key={user.uuid}>
                      <i className="fa fa-user"></i> {user.name} <br></br> <span className="other-users-chips fadeIn"> {user.chips} </span>
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
                      <img className="state-table-card" src={"./res/PNG-cards-1.3/" + table.flop0 + ".png"}></img>
                      <img className="state-table-card" src={"./res/PNG-cards-1.3/" + table.flop1 + ".png"}></img>
                      <img className="state-table-card" src={"./res/PNG-cards-1.3/" + table.flop2 + ".png"}></img>
                      <img className="state-table-card flip-true" src={"./res/PNG-cards-1.3/" + table.turn + ".png"}></img>
                      <img className="state-table-card down-true" src={"./res/PNG-cards-1.3/" + table.river + ".png"}></img>
                  </div>
                </div>)}
              </div>
            

            {/* Display This User's State  */}
            <div id="state-this-user">
                <div id="state-this-user-info"> <i className="fa fa-user"></i> {thisUser.name} <br></br> <span id = "state-this-user-info-chips" className="fadeIn"> {thisUser.chips} </span> </div>
                <div id="state-this-user-action">
                    <span id="state-this-user-action-check" onclick="makeTurn('check')"> <i className="fas fa-check"></i> check </span> 
                    <span id="state-this-user-action-call" onclick="makeTurn('check')"> <i className="fas fa-arrow-right"></i> call </span> 
                    <span id="state-this-user-action-raise" onclick="makeTurn('check')"> <i className="fas fa-arrow-up"></i> raise <input type="text" value="0"></input> </span> 
                    <span id="state-this-user-action-fold" onclick="makeTurn('check')"> <i className="fas fa-arrow-down"></i> fold </span> 
                    <span id="state-this-user-action-allin" onclick="makeTurn('check')"> <i className="fas fa-rocket"></i> all in </span> 
                </div>
                <div id="state-this-user-hand">
                        <img className="state-this-user-card" src={"./res/PNG-cards-1.3/" + thisUser.card0 + ".png"}></img>
                        <img className="state-this-user-card" src={"./res/PNG-cards-1.3/" + thisUser.card1 + ".png"}></img>
                </div>
            </div>



        </body>

      </html>
      
    );
  }
}

export default App;