import React, { Component } from 'react';
import TextField from 'material-ui/TextField';
import Button from 'material-ui/Button';

import Utils from '../Utils';
import Mediator from '../Mediator';

const styles = {
  container: {
    padding: 25,
  },
  field: {
    width: "85%"
  },
  button: {
    marginLeft: 30
  }
};

export default class MenuHolder extends Component {
  state = {
    text: ""
  };

  handleChange = name => event => {
    this.setState({
      [name]: event.target.value,
    });
  };

  componentWillReceiveProps(props) {
    const socket = new WebSocket("ws://" + props.host + "/messages");

    socket.onopen = () => {
      console.log("Connection established");
      socket.send(JSON.stringify({
        className: "otus.project.horizontal_scaling_chat.db_node.frontend.websocket.MessageWS$Token",
        value: Utils.getAuthToken()
      }));
    };

    socket.onclose = (event) => {
      console.log("Connection closed");
    };

    socket.onmessage = (event) => {
      const msg = JSON.parse(event.data);
      Mediator.handle("addMessage", [msg])
    };

    this.setState({
      socket: socket,
      channelId: props.channelId
    })
  }

  // todo transient
  getHost = (host) => {
    return host
  };

  handleSend = () => {
    this.setState({text: ""});

    if (this.state.socket == null) return;

    const msg = {
      className: "otus.project.horizontal_scaling_chat.db_node.db.dataset.Message",
      channel: {
        id: this.state.channelId
      },
      text: this.state.text
    };
    this.state.socket.send(JSON.stringify(msg));
  };

  render() {
    return (
      <div style={styles.container}>
        <TextField style={styles.field}
          id="input"
          label="Write your message"
          value={this.state.text}
          onChange={this.handleChange('text')}
          margin="normal"
          fullWidth
        />
        <Button style={styles.button} onClick={this.handleSend} raised color="primary" >
          Send
        </Button>
      </div>
    )
  }
}