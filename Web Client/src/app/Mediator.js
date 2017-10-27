export default class Mediator {
  static state = {
    addMessage: (message) => {},
    chooseChannel: (id, name, host) => {}
  };

  static handle = (funcName, args) => {
    Mediator.state[funcName].apply(this, args);
  };

  static set = (state) => {
    Object.assign(Mediator.state, state);
  }
}
