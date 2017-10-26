import React, { Component } from 'react';
import TextField from 'material-ui/TextField';
import Button from 'material-ui/Button';

const styles = {
  container: {
    padding: 25,
  },
  field: {
    width: "85%"
  },
  button: {
    marginLeft: 30
  }
};

export default class MenuHolder extends Component {
  render() {
    return (
      <div style={styles.container}>
        <TextField style={styles.field}
          id="input"
          label="Write your message"
          // value={this.state.name}
          // onChange={this.handleChange('name')}
          margin="normal"
          fullWidth
          multiline
          rowsMax="4"
        />
        <Button style={styles.button} raised color="primary" >
          Send
        </Button>
      </div>
    )
  }
}