import React, { Component } from 'react';
import AppBar from 'material-ui/AppBar';
import Toolbar from 'material-ui/Toolbar';
import Button from 'material-ui/Button';
import Typography from 'material-ui/Typography';
import Menu, { MenuItem } from 'material-ui/Menu';

import Utils from '../Utils';

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
    anchorEl: null,
    open: false,
  };

  handleClick = event => {
    this.setState({ open: true, anchorEl: event.currentTarget });
  };

  handleRequestClose = () => {
    this.setState({open: false});
  };

  handleChannelLeave = (id) => {
    fetch(Utils.getMasterHost() + 'api/channels/' + id, {
      method: "DELETE",
      headers: {
        "Authorization": "Bearer " + Utils.getAuthToken()
      }
    })
      .then(res => {
        this.fetch();
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

          <Button
            color="contrast"
            aria-owns={this.state.open ? 'simple-menu' : null}
            aria-haspopup="true"
            onClick={this.handleClick}
          >
            Members
          </Button>
          <Menu
            id="simple-menu"
            anchorEl={this.state.anchorEl}
            open={this.state.open}
            onRequestClose={this.handleRequestClose}
          >
            <MenuItem onClick={this.handleRequestClose}>Member 1</MenuItem>
            <MenuItem onClick={this.handleRequestClose}>Member 2</MenuItem>
            <MenuItem onClick={this.handleRequestClose}>Member 3</MenuItem>
            <MenuItem onClick={this.handleRequestClose}>Member 4</MenuItem>
            <MenuItem onClick={this.handleRequestClose}>Member 5</MenuItem>
            <MenuItem onClick={this.handleRequestClose}>Member 6</MenuItem>
          </Menu>
        </Toolbar>
      </AppBar>
    )
  }
}