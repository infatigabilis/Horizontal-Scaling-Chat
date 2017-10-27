import React, { Component } from 'react';
import { ListItem, ListItemText } from 'material-ui/List';

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
        <ListItemText style={styles.text} primary={<a><b>{this.props.data.sender.login}:</b> {this.props.data.text}</a>} />
      </ListItem>
    )
  }
}