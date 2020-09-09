import React, { Component } from "react";
 
class InputTextItems extends Component {
    constructor(properties) {
        super(properties);
     
        this.createItems = this.createItems.bind(this);
    }
    
    delete(key) {
        this.props.delete(key);
    }

    createItems(item) {
      return <li key={item.key}><div>{item.text}</div><a onClick={() => this.delete(item.key)}>x</a></li>
    }
 
  render() {
    var listItems = this.props.entries.map(this.createItems);
    return (
      <ul className="theList">
          {listItems}
      </ul>
    );
  }
};
 
export default InputTextItems;
