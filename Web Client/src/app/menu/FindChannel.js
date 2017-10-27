import React, { Component } from 'react';
import Button from 'material-ui/Button';
import TextField from 'material-ui/TextField';
import Dialog, {
  DialogActions,
  DialogContent,
  DialogTitle,
} from 'material-ui/Dialog';

import Utils from '../Utils';
import ChannelDialog from './ChannelDialog';

const styles = {
  button: {
    margin: 5
  }
};

export default class FindChannel extends Component {
  state = {
    channelName: "",
    open: false,
    openCreate: false,
  };

  handleChange = name => event => {
    this.setState({
      [name]: event.target.value,
    });
  };

  handleCreateChannel = (name) => {
    fetch(Utils.getMasterHost() + 'api/channels', {
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        "Authorization": "Bearer " + Utils.getAuthToken()
      },
      method: "POST",
      body: JSON.stringify({
        "name": this.state.channelName
      })
    })
      .then((res) => {
        this.setState({
          channelName: "",
          openCreate: false
        });
        this.props.parentFetch();
      })
  };

  render() {
    return (
      <div>
        <Button style={styles.button} raised color="primary" onClick={this.handleCreateClickOpen}>
          Create channel
        </Button>
        <Dialog open={this.state.openCreate} onRequestClose={this.handleCreateRequestClose}>
          <DialogTitle>Create channel</DialogTitle>
          <DialogContent style={{width: 400}}>
            <TextField
              autoFocus
              margin="dense"
              id="name"
              label="Channel name"
              value={this.state.channelName}
              onChange={this.handleChange('channelName')}
              fullWidth
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleCreateRequestClose} color="primary">
              Cancel
            </Button>
            <Button onClick={this.handleCreateChannel} color="primary">
              Create
            </Button>
          </DialogActions>
        </Dialog>


        <Button style={styles.button} onClick={this.handleClickOpen}>
          Find channel
        </Button>
        <ChannelDialog
          selectedValue={this.state.selectedValue}
          open={this.state.open}
          onRequestClose={this.handleRequestClose}
        />
      </div>
    )
  }

  handleClickOpen = () => {
    this.setState({
      open: true,
    });
  };

  handleRequestClose = value => {
    this.props.parentFetch();
    this.setState({ selectedValue: value, open: false });
  };

  handleCreateClickOpen = () => {
    this.setState({
      openCreate: true,
    });
  };

  handleCreateRequestClose = value => {
    this.setState({openCreate: false });
  };
}