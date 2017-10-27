import React, { Component } from 'react';
import Paper from 'material-ui/Paper';

import Utils from '../Utils';
import Mediator from '../Mediator';

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
  state = {
    id: null,
    name: "",
    host: null,
    messages: []
  };

  constructor(props) {
    super(props);
    Mediator.set({
      chooseChannel: this.chooseChannel,
      addMessage: this.addMessage
    })
  }

  addMessage = (msg) => {
    const messages = this.state.messages;
    messages.unshift(msg);

    this.setState({
      messages: messages
    })
  };

  chooseChannel = (id, name, host) => {
    this.fetch(id, name, host);
  };

  // todo handle page
  fetch = (id, name, host) => {
    fetch(Utils.transformDbHost(host) + 'api/' + id + '/messages?page=0', {
      headers: {
        "Authorization": "Bearer " + Utils.getAuthToken()
      }
    })
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({
          id: id,
          name: name,
          host: host,
          messages: json
        });
      })
  };

  render() {
    return (
      <Paper style={styles.holder} elevation={10}>
        <ChannelName name={this.state.name} channelId={this.state.id} />
        <Messages messages={this.state.messages} />
        <Input host={this.state.host} channelId={this.state.id} />
      </Paper>
    )
  }
}