import React, { Component } from 'react';
import List, { ListItem, ListItemIcon, ListItemText } from 'material-ui/List';

const styles = {
  text: {
    textAlign: "left"
  },
};

export default class MenuHolder extends Component {
  render() {
    return (
      <ListItem
        button
        divider
      >
        <ListItemText style={styles.text} primary="Long message text" />
      </ListItem>
    )
  }
}