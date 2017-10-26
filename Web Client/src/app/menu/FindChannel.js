import React, { Component } from 'react';
import Button from 'material-ui/Button';

const styles = {
  paper: {
    padding: 10,
    margin: "auto",
    width: "50%"
  }
};

export default class FindChannel extends Component {
  render() {
    return (
      <Button >
        Найти конференции
      </Button>
    )
  }
}