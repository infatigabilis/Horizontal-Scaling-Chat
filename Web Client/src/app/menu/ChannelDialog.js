import React from 'react'
import List, { ListItem, ListItemSecondaryAction, ListItemText } from 'material-ui/List';
import Dialog, {
  DialogTitle,
} from 'material-ui/Dialog';

import Utils from "../Utils";
import Button from 'material-ui/Button'

const styles = {
  listText: {
    fontSize: 20
  }
};

export default class SimpleDialog extends React.Component {
  state = {
    channels: []
  };

  componentDidMount() {
    this.fetch();
  }

  fetch = () => {
    fetch(Utils.getMasterHost() + 'api/channels')
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({
          channels: json
        });
      })
  };

  handleChannelJoin = (id) => {
    fetch(Utils.getMasterHost() + 'api/channels/' + id, {
      method: "PUT",
      headers: {
        "Authorization": "Bearer " + Utils.getAuthToken()
      }
    })
      .then(res => {
        this.handleListItemClick("");
      })
  };

  render() {
    const { classes, onRequestClose, selectedValue, ...other } = this.props;

    const channels = this.state.channels.map(channel => {
      return (
        <ListItem key={channel.id} button divider>
          <ListItemText primary={<a style={styles.listText}>{channel.name}</a>} />
          <ListItemSecondaryAction>
            <Button color="primary" onClick={() => this.handleChannelJoin(channel.id)} >
              Join
            </Button>
          </ListItemSecondaryAction>
        </ListItem>
      )
    });

    return (
      <Dialog onRequestClose={this.handleRequestClose} {...other}>
        <DialogTitle>All channels</DialogTitle>
        <div>
          <List style={{width: 500}}>
            {channels}
          </List>
        </div>
      </Dialog>
    );
  }

  handleRequestClose = () => {
    this.props.onRequestClose(this.props.selectedValue);
  };

  handleListItemClick = value => {
    this.props.onRequestClose(value);
  };
}