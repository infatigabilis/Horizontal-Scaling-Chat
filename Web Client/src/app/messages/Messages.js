import React, { Component } from 'react';
import List, { ListItem, ListItemIcon, ListItemText } from 'material-ui/List';

import Message from './Message';

const styles = {
  list: {
    height: '79%',
    padding: '10px 35px 0 35px',
  }
};

export default class MenuHolder extends Component {
  render() {
    return (
      <div style={styles.list}>
        <List>
          <Message/>
          <Message/>
          <Message/>
        </List>
      </div>
    )
  }
}