import React, {Component} from 'react';
import InputTextItems from './InputTextItems'
import './InputText.css'

class InputText extends Component {
    constructor(properties) {
        super(properties);
       
        this.state = {
          items: []
        };
       
        this.addItem = this.addItem.bind(this);
        this.deleteItem = this.deleteItem.bind(this);

    }

    addItem(event) {
      if (this._inputElement.value === "") {
        return;
      }
      var newItem = {
        text: this._inputElement.value.charAt(0).toUpperCase()
            + this._inputElement.value.slice(1).toLowerCase(),
        key: Date.now()
      };
      var isDuplicate = false;
      this.setState((prevState) => {
        prevState.items.forEach(function(item, index, object) {
          if (item.text===newItem.text){
            isDuplicate=true;
            window.alert("Duplicate Ingredient");
          }
        });
        if (isDuplicate) {
          return { 
            items: prevState.items
          };
        } else {
          return { 
            items: [newItem].concat(prevState.items)
          };
        }
      });
      this._inputElement.value = "";
      event.preventDefault();
    }

    deleteItem(key) {
        var filteredItems = this.state.items.filter(function (item) {
          return (item.key !== key);
        });
       
        this.setState({
          items: filteredItems
        });
    }
    
    render() {
        return (
          <div className="ingredientList">
            <div className="header">
              <h1>Input Ingredients</h1>
              <form onSubmit={this.addItem}>
              <input ref={(a) => this._inputElement = a} placeholder="New Ingredient">
                </input>
                <button type="submit">add</button>
              </form>
            </div>
            <InputTextItems entries={this.state.items} delete={this.deleteItem}/>
          </div>
        );
      }
    }
export default InputText;
