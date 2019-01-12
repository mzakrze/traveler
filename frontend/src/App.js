import React, { Component } from 'react';
import logo from './logo.svg';
import LeafletComponent from './components/LeafletComponent.js';
import FormComponent from './components/FormComponent.js';
import ResultPresenterComponent from './components/ResultPresenterComponent.js';

export type FindRouteResult = {
    directions: Array<string>,
    selectedPlaces: Array<any>,
    places: any,
    startLocation: any,
    endLocation: any,
    // TODO - dodać place details
}

const APP_INSTRUCTIONS = "To set your start/end location please:\n - click on 'Set your start location'\n - left click on map"

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
            resultPresenterVisible: false,
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
        let directions = [];
        for(let d of result.directions) {
            directions.push(JSON.parse(d))
        }
        let res = {
            directions: directions,
            places: JSON.parse(result.places),
            selectedPlaces: result.selectedPlaces,
            startLocation: result.startLocation,
            endLocation:  result.endLocation
        }
        this.setState({
            findRouteResult: res,
            findRouteResultRev: rev,
            resultPresenterVisible: true,
        });
    }

    getColor(index) {
        // wybrane takie bardziej kontrastujące
        const colors = ['#e6194b', '#3cb44b', '#ffe119', '#4363d8', '#f58231', '#911eb4', '#46f0f0', '#f032e6', '#bcf60c', '#fabebe', '#008080', '#e6beff', '#9a6324', '#fffac8', '#800000', '#aaffc3', '#808000', '#ffd8b1', '#000075', '#808080', '#ffffff'];
        let randomColor = () => {
            const possible = "0123456789abcdef";
            let r = () => possible[Math.floor(Math.random() * 16)];
            return "#" + r() + r() + r();
        }
        if(colors.length <= index) {
            let c = randomColor()
            colors.push(c);
            return c;
        }
        return colors[index];
    }

    render() {
        let formComponentStyleDisplay = this.state.resultPresenterVisible ? 'none' : 'inline-block';
        let resultPresenterStyleDisplay = this.state.resultPresenterVisible == false ? 'none' : 'inline-block';
        return  <div>
           <span>
            <button type="button" onClick={() => alert(APP_INSTRUCTIONS)}> Show instructions </button>
            <br />
               <div style={{display:'inline-block', marginRight:'20px'}}>
                   <LeafletComponent
                        handleClickOnMap={this.handleClickOnMap.bind(this)}
                        startLocation={this.state.startLocation}
                        endLocation={this.state.endLocation}
                        findRouteResult={this.state.findRouteResult}
                        findRouteResultRev={this.state.findRouteResultRev}
                        getColor={this.getColor}
                        />
               </div>
               <div style={{display:formComponentStyleDisplay, width: '600px'}}>
                   <FormComponent
                        lastMapClickCoords={this.state.lastMapClickCoords}
                        notifyNewStartLocation={this.handleNewStartLocation.bind(this)}
                        notifyNewEndLocation={this.handleNewEndLocation.bind(this)}
                        notifyFindRouteResult={this.handleFindRouteResult.bind(this)}
                         />
               </div>
               <div style={{display:resultPresenterStyleDisplay, width: '600px'}}>
                  <ResultPresenterComponent
                       notifyChangeScreen={() => this.setState({resultPresenterVisible: false})}
                       result={this.state.findRouteResult}
                       getColor={this.getColor}
                        />
               </div>
           </span>
        </div>;
    }
}

export default App;
