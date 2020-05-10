import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import 'font-awesome/css/font-awesome.min.css'

class App extends Component {
  state = {
    // isLoading: true,
    users: []
  };

  componentDidMount() {
    this.intervalId = setInterval(() => this.loadData(), 1000);
  }

  componentWillUnmount() {
      clearInterval(this.intervalId);
  }

  loadData() {
      fetch('/users')
      .then((response) => response.json())
      .then((responseJson) => {
        this.setState({users: responseJson, isLoading: false});
      })
      .catch((error) => {
        console.error(error);
      });
  }

  render() {
    const {users, isLoading} = this.state;

    if (isLoading) {
      return <p>loading</p>;
    }

    return (

      <html id = "room">

        <head>
          {/* <script src="https://kit.fontawesome.com/096f2be9ca.js" crossOrigin="anonymous"></script> REDUNDANT? */}
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

        {/* < Display Table State  */}
        <div id="state-table">
            <div id="state-table-info">
                    <span id="state-table-info-pot" className="fadeInUp"> <i className="fas fa-piggy-bank"></i> 105 </span> 
                    <span id="state-table-info-bet" className="fadeInUp"> <i className="fas fa-dollar-sign"></i> 15 </span> 
            </div>
            <div id="state-table-hand">
                <img className="state-table-card" src="./res/PNG-cards-1.3/8_of_clubs.png"></img>
                <img className="state-table-card" src="./res/PNG-cards-1.3/10_of_diamonds.png"></img>
                <img className="state-table-card" src="./res/PNG-cards-1.3/4_of_diamonds.png"></img>
                <img className="state-table-card flip-true" src="./res/PNG-cards-1.3/reverse.png"></img>
                <img className="state-table-card down-true" src="./res/PNG-cards-1.3/reverse.png"></img>
            </div>
        </div>

        </body>

      </html>
      
    );
  }
}

export default App;