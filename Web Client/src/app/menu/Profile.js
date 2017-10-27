import React, { Component } from 'react';
import Paper from 'material-ui/Paper';
import Typography from 'material-ui/Typography';

const styles = {
  paper: {
    padding: 18,
    margin: 0,
    background: "#3F51B5",
    color: "#F5F5F5"
  }
};

export default class Profile extends Component {
  render() {
    return (
      <Paper style={styles.paper} elevation={4}>

        <Typography color="inherit" type="Display3" component="h2">
          Firstname Lastname
        </Typography>
      </Paper>
    )
  }
}