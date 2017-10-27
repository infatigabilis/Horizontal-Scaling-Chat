import config from "../config"

export default class Utils {
  static getAuthToken = () => {
    return "ya29.GlzvBC4js1CZTFiW0yldxDOFKQMd0vkiB-BsfboN8f_ESJvQsShzLxSFEe3VDdUuyMTlNp4O1rgtHauUwfkgO-LXPUEpn9ayZddwMw5h250qYsDccgL1h_bYiguPOg";
  };

  static getMasterHost = () => {
    return "http://" + config.masters[0] + "/";
  };

  // todo transient
  static transformDbHost(host) {
    return "http://" + host + "/";
  }
}
