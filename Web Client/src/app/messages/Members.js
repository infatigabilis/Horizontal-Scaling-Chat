import React, { Component } from 'react';
import Button from 'material-ui/Button';
import Menu, { MenuItem } from 'material-ui/Menu';

import Utils from '../Utils';

export default class Profile extends Component {
  state = {
    members: [],
    anchorEl: null,
    open: false,
  };

  componentDidMount() {
    this.fetch();
  }

  fetch = () => {
    fetch(Utils.getMasterHost() + 'api/channels/' + this.props.channelId + '/members', {
      headers: {
        "Authorization": "Bearer " + Utils.getAuthToken()
      }
    })
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({
          members: json
        });
      })
  };

  handleClick = event => {
    this.setState({ open: true, anchorEl: event.currentTarget });
    this.fetch();
  };

  handleRequestClose = () => {
    this.setState({open: false});
  };

  render() {
    const members = this.state.members.map(member => {
      return <MenuItem onClick={this.handleRequestClose}>{member.login}</MenuItem>
    });

    return (
      <div>
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
          {members}
        </Menu>
      </div>
    )
  }
}
