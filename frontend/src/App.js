import React, { Component } from 'react';
import logo from './logo.svg';
import LeafletComponent from './components/LeafletComponent.js';
import FormComponent from './components/FormComponent.js';

class App extends Component {
    render() {
        return  <div>
           <h2> Title to be done </h2>
           <span>
               <div style={{display:'inline-block', marginRight:'20px'}}>
                   <LeafletComponent />
               </div>
               <div style={{display:'inline-block', width: '600px'}}>
                   <FormComponent />
               </div>
           </span>
        </div>;
    }
}

export default App;
