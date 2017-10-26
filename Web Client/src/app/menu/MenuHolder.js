import React, { Component } from 'react';
import List, { ListItem, ListItemIcon, ListItemText } from 'material-ui/List';
import Profile from "./Profile";
import FindChannel from "./FindChannel";

const styles = {
  holder: {
    height: '100%',
    position: 'fixed',
    width: '20%',
    top: 0,
    overflow: 'auto'
  },
  list: {
    height: "80%"
  }
};

export default class MenuHolder extends Component {
  render() {
    return (
      <div style={styles.holder}>
        <Profile/>
        <List style={styles.list}>
          <ListItem button>
            <ListItemText primary="Channel 1" />
          </ListItem>
          <ListItem button>
            <ListItemText primary="Channel 2" />
          </ListItem>
          <ListItem button>
            <ListItemText primary="Channel 3" />
          </ListItem>
        </List>
        <FindChannel/>
      </div>
    )
  }
}