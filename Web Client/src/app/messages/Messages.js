import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import List from 'material-ui/List';
import Message from './Message';

const styles = {
  list: {
    height: 793,
    padding: '10px 35px 0 35px',
    overflow: "auto"
  }
};

export default class MenuHolder extends Component {
  state = {
    messages: []
  };

  componentWillReceiveProps(props) {
    this.setState({
      messages: props.messages
    });
  }

  render() {
    const messages = this.state.messages.slice(0).reverse().map(msg => {
      return <Message key={msg.id} data={msg} />
    });

    return (
      <div style={styles.list}>
        <List>
          {messages}
        </List>
        <div style={{ float:"left", clear: "both" }}
             ref={(el) => { this.messagesEnd = el; }}>
        </div>
      </div>
    )
  }

  scrollToBottom = () => {
    const node = ReactDOM.findDOMNode(this.messagesEnd);
    node.scrollIntoView({ behavior: "smooth" });
  }

  componentDidMount() {
    this.scrollToBottom();
  }

  componentDidUpdate() {
    this.scrollToBottom();
  }
}