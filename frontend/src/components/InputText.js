import React, {Component} from 'react';
import InputTextItems from './InputTextItems'
import './InputText.css'

class InputText extends Component {
    constructor(props) {
        super(props);
       
        this.state = {
          items: []
        };
       
        this.addItem = this.addItem.bind(this);
        this.deleteItem = this.deleteItem.bind(this);

      }

    addItem(e) {
        if (this._inputElement.value !== "") {
          var newItem = {
            text: this._inputElement.value,
            key: Date.now()
          };
       
          this.setState((prevState) => {
            return { 
              items: prevState.items.concat(newItem) 
            };
          });
         
          this._inputElement.value = "";
        }
         
           
        e.preventDefault();
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
