import React, { Component } from 'react';
import AppBar from 'material-ui/AppBar';
import Toolbar from 'material-ui/Toolbar';
import Button from 'material-ui/Button';
import Typography from 'material-ui/Typography';

import Utils from '../Utils';
import Mediator from '../Mediator';
import Members from './Members';

const styles = {
  paper: {
    margin: 0,
  },
  text: {
    fontSize: 24,
    flex: 1,
  }
};

export default class Profile extends Component {
  state = {
    name: "",
  };

  handleChannelLeave = (id) => {
    fetch(Utils.getMasterHost() + 'api/channels/' + id, {
      method: "DELETE",
      headers: {
        "Authorization": "Bearer " + Utils.getAuthToken()
      }
    })
      .then(res => {
        Mediator.handle("messageFetch", []);
      })
  };

  render() {
    return (
      <AppBar style={styles.paper} position="static">
        <Toolbar>
          <Button raised color="accent" onClick={() => this.handleChannelLeave(this.props.channelId)} >
            Leave
          </Button>

          <Typography type="title" color="inherit" style={styles.text}>
            {this.props.name}
          </Typography>

          <Members channelId={this.props.channelId} />
        </Toolbar>
      </AppBar>
    )
  }
}