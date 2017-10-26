import React from 'react'
import List, { ListItem, ListItemSecondaryAction, ListItemText } from 'material-ui/List';
import Dialog, {
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from 'material-ui/Dialog';
import Button from 'material-ui/Button'

export default class SimpleDialog extends React.Component {
  handleRequestClose = () => {
    this.props.onRequestClose(this.props.selectedValue);
  };

  handleListItemClick = value => {
    this.props.onRequestClose(value);
  };

  render() {
    const { classes, onRequestClose, selectedValue, ...other } = this.props;

    return (
      <Dialog onRequestClose={this.handleRequestClose} {...other}>
        <DialogTitle>All channels</DialogTitle>
        <div>
          <List style={{width: 500}}>
            <ListItem button onClick={() => this.handleListItemClick("")}>
              <ListItemText primary="Channel 10" />
              <ListItemSecondaryAction>
                <Button color="primary">
                  Join
                </Button>
              </ListItemSecondaryAction>
            </ListItem>
            <ListItem button onClick={() => this.handleListItemClick("")}>
              <ListItemText primary="Channel 11" />
              <ListItemSecondaryAction>
                <Button color="primary">
                  Join
                </Button>
              </ListItemSecondaryAction>
            </ListItem>
            <ListItem button onClick={() => this.handleListItemClick("")}>
              <ListItemText primary="Channel 12" />
              <ListItemSecondaryAction>
                <Button color="primary">
                  Join
                </Button>
              </ListItemSecondaryAction>
            </ListItem>
          </List>
        </div>
      </Dialog>
    );
  }
}