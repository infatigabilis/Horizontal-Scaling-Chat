import React, { Component } from 'react';
import Paper from 'material-ui/Paper';
import Typography from 'material-ui/Typography';

import Utils from '../Utils';

const styles = {
  paper: {
    padding: 18,
    margin: 0,
    background: "#3F51B5",
    color: "#F5F5F5"
  }
};

export default class Profile extends Component {
  state = {
    nickname: ""
  };

  componentDidMount() {
    this.fetch();
  }

  fetch = () => {
    fetch(Utils.getMasterHost() + 'api/users/me', {
      headers: {
        "Authorization": "Bearer " + Utils.getAuthToken()
      }
    })
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({
          nickname: json.login
        });
      })
  };

  render() {
    return (
      <Paper style={styles.paper} elevation={4}>

        <Typography color="inherit" type="Display3" component="h2">
          {this.state.nickname}
        </Typography>
      </Paper>
    )
  }
}