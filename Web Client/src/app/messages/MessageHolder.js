import React, { Component } from 'react';
import Paper from 'material-ui/Paper';

import Input from './Input';
import Messages from './Messages';
import ChannelName from './ChannelName';

const styles = {
  holder: {
    height: '100%',
    position: 'fixed',
    width: '79%',
    top: 0,
    left: "20%",
  }
};

export default class MenuHolder extends Component {
  render() {
    return (
      <Paper style={styles.holder} elevation={10}>
        <ChannelName/>
        <Messages/>
        <Input/>
      </Paper>
    )
  }
}