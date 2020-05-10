import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

class App extends Component {
  state = {
    // isLoading: true,
    groups: []
  };

  // async componentDidMount() {
  //   const response = await fetch('/users');
  //   const body = await response.json();
  //   this.setState({ groups: body, isLoading: false });
  // }

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
        this.setState({groups: responseJson, isLoading: false});
      })
      .catch((error) => {
        console.error(error);
      });
  }

  render() {
    const {groups, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <div className="App-intro">
            <h2>JUG List</h2>
            {groups.map(group =>
              <div key={group.uuid}>
                {group.name}
              </div>
            )}
          </div>
          {/* <div>
            {this.state.time}
          </div> */}
        </header>
      </div>
    );
  }
}

export default App;