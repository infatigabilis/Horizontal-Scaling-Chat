import React, { Component } from 'react';
import Button from 'material-ui/Button';
import TextField from 'material-ui/TextField';
import Dialog, {
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from 'material-ui/Dialog';

import ChannelDialog from './ChannelDialog';

const styles = {
  paper: {
    padding: 10,
    margin: "auto",
    width: "50%"
  }
};

const emails = ['username@gmail.com', 'user02@gmail.com'];

export default class FindChannel extends Component {
  state = {
    open: false,
    openCreate: false,
    selectedValue: emails[1],
  };

  handleClickOpen = () => {
    this.setState({
      open: true,
    });
  };

  handleRequestClose = value => {
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


  render() {
    return (
      <div>
        <Button onClick={this.handleCreateClickOpen}>
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
              fullWidth
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleCreateRequestClose} color="primary">
              Cancel
            </Button>
            <Button onClick={this.handleCreateRequestClose} color="primary">
              Create
            </Button>
          </DialogActions>
        </Dialog>


        <Button onClick={this.handleClickOpen}>
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
}