import React from 'react';
import List, { ListItem, ListItemText, } from 'material-ui/List';
import Typography from 'material-ui/Typography';

import Utils from "../Utils";
import Mediator from "../Mediator";
import Profile from "./Profile";
import FindChannel from "./FindChannel";

const styles = {
  holder: {
    height: '100%',
    position: 'fixed',
    width: '20%',
    top: 0,
    overflow: 'auto',
  },
  list: {
    height: "83%",
  },
  listText: {
  },
};

export default class MenuHolder extends React.Component {
  state = {
    channels: []
  };

  componentDidMount() {
    this.fetch()
  }

  fetch = () => {
    fetch(Utils.getMasterHost() + 'api/channels/my', {
      headers: {
        "Authorization": "Bearer " + Utils.getAuthToken()
      }
    })
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({
          channels: json
        });
        Mediator.handle("chooseChannel", [json[0].id, json[0].name, json[0].host]);
      })
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
    const channels = this.state.channels.map(channel => {
      return (
        <ListItem style={styles.listItem}
          key={channel.id}
          onClick={() => {
            Mediator.handle("chooseChannel", [channel.id, channel.name, channel.host])
          }}
          button
          divider
        >
          {/*<a style={styles.listText}>{channel.name}</a>*/}
          <ListItemText
            primary={
              <Typography align="left" style={styles.listText} color="inherit" type="title" component="h3">
                {channel.name}
              </Typography>
            }
          />
        </ListItem>
      )
    });

    return (
      <div style={styles.holder}>
        <Profile/>
        <br/>
        <List style={styles.list}>
          {channels}
        </List>
        <FindChannel parentFetch={this.fetch} />
      </div>
    )
  }
}