import config from "../config"

export default class Utils {
  static getAuthToken = () => {
    if (localStorage.getItem("token") === null) Utils.auth();

    return localStorage.getItem("token");
  };

  static auth() {
    const authUrl = 'https://accounts.google.com/o/oauth2/v2/auth?client_id=764305920855-mvd0tmc97rvpn2mmgvr5r4b1th1t13a7.apps.googleusercontent.com&redirect_uri=http://localhost:9001/api/auth&scope=https://www.googleapis.com/auth/plus.me&response_type=code';
    window.location.href = authUrl;
  }

  static getMasterHost = () => {
    return "/";
    // return "http://" + config.masters[0] + "/";
  };

  static transformDbHost(host) {
    return "http://" + host + "/";
  }
}
