import React, { Component } from 'react';
import logo from './logo.svg';
import LeafletComponent from './components/LeafletComponent.js';
import FormComponent from './components/FormComponent.js';

type FindRouteResult = {
    directions: string,
}

class App extends Component {

    props: Props;
    state: State;

    constructor(props: Props){
        super(props);
        this.state = {
            lastMapClickCoords: null,
            startLocation: null,
            endLocation: null,
            findRouteResult: null,
            findRouteResultRev: 0,
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

    handleFindRouteResult(result: FindRouteResult) {
        let rev = this.state.findRouteResultRev + 1;
        let res = {
            directions: JSON.parse(result.directions)
        }
        this.setState({
            findRouteResult: res,
            findRouteResultRev: rev,
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
                        findRouteResult={this.state.findRouteResult}
                        findRouteResultRev={this.state.findRouteResultRev}
                        />
               </div>
               <div style={{display:'inline-block', width: '600px'}}>
                   <FormComponent
                        lastMapClickCoords={this.state.lastMapClickCoords}
                        notifyNewStartLocation={this.handleNewStartLocation.bind(this)}
                        notifyNewEndLocation={this.handleNewEndLocation.bind(this)}
                        notifyFindRouteResult={this.handleFindRouteResult.bind(this)}
                         />
               </div>
           </span>
        </div>;
    }
}

export default App;
