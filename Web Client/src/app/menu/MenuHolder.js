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
    height: "82%"
  }
};

export default class MenuHolder extends Component {
  render() {
    return (
      <div style={styles.holder}>
        <Profile/>
        <br/>
        <List style={styles.list}>
          <ListItem button>
            <ListItemText primary={<a style={{fontSize: 20}}>Channel 1</a>} />
          </ListItem>
          <ListItem button>
            <ListItemText primary={<a style={{fontSize: 20}}>Channel 2</a>} />
          </ListItem>
          <ListItem button>
            <ListItemText primary={<a style={{fontSize: 20}}>Channel 3</a>} />
          </ListItem>
        </List>
        <FindChannel/>
      </div>
    )
  }
}