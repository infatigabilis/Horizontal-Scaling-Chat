import React, { Component } from 'react';

import './App.css';
import 'typeface-roboto';

import MenuHolder from './app/menu/MenuHolder';
import MessageHolder from './app/messages/MessageHolder';

class App extends Component {

  render() {
    return (
      <div className="App">
        <MenuHolder />
        <MessageHolder />
      </div>
    );
  }
}

export default App;
