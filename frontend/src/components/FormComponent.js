/* @flow */
import React, { Component } from 'react';

export default class FormComponent extends Component {
    props: Props;
    state: State;

    constructor(props: Props){
        super(props);
        this.state = {
            checkedCheckboxes: [],
            startLocation: null,
            awaitingForStartLocation: false,
            endLocation: null,
            awaitingForEndLocation: false,
        }
    }

    componentWillReceiveProps(nextProps:  Props) {
        if(this.state.props !== nextProps.lastMapClickCoords){

            if(this.state.awaitingForStartLocation) {
                this.setState({
                    awaitingForStartLocation: false,
                    startLocation: nextProps.lastMapClickCoords
                });
                this.props.notifyNewStartLocation(nextProps.lastMapClickCoords);
            }

            if(this.state.awaitingForEndLocation) {
                this.setState({
                    awaitingForEndLocation: false,
                    endLocation: nextProps.lastMapClickCoords
                });
                this.props.notifyNewEndLocation(nextProps.lastMapClickCoords);
            }
        }
    }

    renderCheckbox(name: string) {
        let handle = (ev) => {
            let checked = this.state.checkedCheckboxes.slice()
            if(ev.target.checked) {
                checked.push(name);
            } else {
                let index = checked.indexOf(name);
                checked.splice(index, 1);
            }
            this.setState({
                checkedCheckboxes: checked
            });
        }
        return [
            <input type="checkbox" id={name} name={name} value={name} onClick={handle}/>,
            <label htmlFor={name}>{name}</label>,
            <br />
        ];
    }

    renderFindRoutesButton() {
        let send = () => {
            let data = {
                placesOfInterest: this.state.checkedCheckboxes,
                startLocation: this.state.startLocation,
                endLocation: this.state.endLocation
            }
            fetch('/api/stub/simple_map_req', {method: 'POST', body: JSON.stringify(data)})
                .then(function(response) {
                    return response.json();
                })
                .then(function(myJson) {
                    console.log(JSON.stringify(myJson));
                });
        }
        return <button type="button" onClick={send}>Ok - find optimal route</button>
    }

    renderStartEndTripLocationsInput() {
        let handleNewStartLocation = () => {
            this.setState({
                awaitingForStartLocation: true,
                awaitingForEndLocation: false,
            });
        }

        let handleNewEndLocation = () => {
            this.setState({
                awaitingForEndLocation: true,
                awaitingForStartLocation: false,
            });
        }

        return [
            <p>Your trip start-point location: </p>,
            this.state.startLocation == null ?
                <p style={{color: 'red'}}>You havent set your start location!</p>
                :
                <p>{JSON.stringify(this.state.startLocation)}</p>
            ,
            this.state.startLocation == null ?
                <button type="button" onClick={handleNewStartLocation}>Set your start position</button>
                :
                <button type="button" onClick={handleNewStartLocation}>Update start position</button>
            ,
            <br />,
            <hr />,
            <p>Your trip end-point location: </p>,
            this.state.endLocation == null ?
                <p style={{color: 'red'}}>You havent set your end location!</p>
                :
                <p>{JSON.stringify(this.state.endLocation)}</p>
            ,
            this.state.endLocation == null ?
                <button type="button" onClick={handleNewEndLocation}>Set your end position</button>
                :
                <button type="button" onClick={handleNewEndLocation}>Update end position</button>
            ,
            <hr />,
        ];
    }


    render(){
        return <form>
            {this.renderStartEndTripLocationsInput()}
            {this.renderCheckbox('restaurants')}
            {this.renderCheckbox('liquor stores')}
            {this.renderCheckbox('museums')}
            {this.renderFindRoutesButton()}
        </form>;
    }

}