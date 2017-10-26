import React, { Component } from 'react';
import Paper from 'material-ui/Paper';
import Typography from 'material-ui/Typography';

const styles = {
  paper: {
    padding: 24,
    margin: 0
  }
};

export default class Profile extends Component {
  render() {
    return (
      <Paper style={styles.paper} elevation={4}>

        <Typography type="headline" component="h2">
          Firstname Lastname
        </Typography>
      </Paper>
    )
  }
}