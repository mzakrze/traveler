import React, { Component } from 'react';
import logo from './logo.svg';
import LeafletComponent from './components/LeafletComponent.js';
import FormComponent from './components/FormComponent.js';

class App extends Component {

    props: Props;
    state: State;

    constructor(props: Props){
        super(props);
        this.state = {
            lastMapClickCoords: null,
            startLocation: null,
            endLocation: null,
        }
    }

    handleClickOnMap(latlng) {
        this.setState({
            lastMapClickCoords: latlng
        });
    }

    handleNewStartLocation(latlng) {
        this.setState({
            startLocation: latlng
        });
    }

    handleNewEndLocation(latlng) {
        this.setState({
            endLocation: latlng
        });
    }

    render() {
        return  <div>
           <h2> Title to be done </h2>
           <span>
               <div style={{display:'inline-block', marginRight:'20px'}}>
                   <LeafletComponent
                        handleClickOnMap={this.handleClickOnMap.bind(this)}
                        startLocation={this.state.startLocation}
                        endLocation={this.state.endLocation}
                        />
               </div>
               <div style={{display:'inline-block', width: '600px'}}>
                   <FormComponent
                        lastMapClickCoords={this.state.lastMapClickCoords}
                        notifyNewStartLocation={this.handleNewStartLocation.bind(this)}
                        notifyNewEndLocation={this.handleNewEndLocation.bind(this)}
                         />
               </div>
           </span>
        </div>;
    }
}

export default App;
