/* @flow */
import React, { Component } from 'react';


// TODO - mapowanie place_code na postać dla użytkownika
const SUPPORTED_PLACES_CODES = ['amusement_park','aquarium','art_gallery','bar','beauty_salon','book_store','bowling_alley','cafe','casino','church','city_hall','clothing_store','gym','hair_care','laundry','meal_takeaway','movie_rental','movie_theater','museum','night_club','park','pharmacy','store','supermarket','travel_agency', 'zoo']


export default class FormComponent extends Component {
    props: Props;
    state: State;

    constructor(props: Props){
        super(props);
        this.state = {
            selectedPlacesTypes: [],
            acceptedTravelModes: [],
            startLocation: null,
            awaitingForStartLocation: false,
            endLocation: null,
            awaitingForEndLocation: false,
            startTimeHour: '',
            startTimeMinute: '',
            tripDuration: '',
            placesKeywords: '',
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
            let checked = this.state.selectedPlacesTypes.slice()
            if(ev.target.checked) {
                checked.push(name);
            } else {
                let index = checked.indexOf(name);
                checked.splice(index, 1);
            }
            this.setState({
                selectedPlacesTypes: checked
            });
        }
        return [
            <input type="checkbox" id={name} name={name} value={name} onClick={handle}/>,
            <label htmlFor={name}>{name}</label>,
            <br />
        ];
    }

    renderFindRoutesButton() {
        let validate = () => {
            let msg = '';
            if(this.state.startLocation == null) {
                msg += 'Set your start location\n';
            }
            if(this.state.endLocation == null) {
                msg += 'Set your end location\n';
            }
            if(this.state.tripDuration == '') {
                msg += 'Set you trip duration\n';
            }

            if(msg != '') {
                alert(msg)
            }
            return msg == ''; // no alert = validation ok
        }

        let send = () => {
            let data = {
                placesOfInterest: this.state.selectedPlacesTypes,
                startLocation: this.state.startLocation,
                endLocation: this.state.endLocation,
                tripStart: this.state.startTimeHour == '' && this.state.startTimeMinute == '' ? null : {hour: this.state.startTimeHour, minute: this.state.startTimeMinute},
                tripDuration: this.state.tripDuration,
                placesKeywords: this.state.placesKeywords,
                travelModes: this.state.acceptedTravelModes
            }
            fetch('/api/maps/find_route', {
                method: 'POST',
                body: JSON.stringify(data),
                headers: {
                      'Accept': 'application/json',
                      'Content-Type': 'application/json'
                },
            })
                .then((response) => {
                    return response.json();
                })
                .then((response) => {
                    if(response.error != null) {
                       alert(response.error);
                    } else {
                        this.props.notifyFindRouteResult(response);
                    }
                });
        }
        return <button type="button" onClick={() => {if(validate()) {send()}}}>Ok - find optimal route</button>
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

        let handleChange = (key, value) => {
            this.setState({[key]: value});
        }

        let renderTravelModeCheckbox = (mode) => {
            let handle = (ev) => {
                let checked = this.state.acceptedTravelModes.slice()
                if(ev.target.checked) {
                    checked.push(mode);
                } else {
                    let index = checked.indexOf(mode);
                    checked.splice(index, 1);
                }
                this.setState({
                    acceptedTravelModes: checked
                });
            }
            return [
                <input type="checkbox" id={mode} name={mode} value={mode} onClick={handle}/>,
                <label htmlFor={mode}>{mode}</label>,
                <br />
            ];
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
            <p>Your trip start time: </p>,
            <label htmlFor="input_start_time_hour">Hour:</label>,
            <input type="text" id="input_start_time_hour" onChange={(ev) => handleChange('startTimeHour', ev.target.value)} />,
            <label htmlFor="input_start_time_minute">Minute:</label>,
            <input type="text" id="input_start_time_minute" onChange={(ev) => handleChange('startTimeMinute', ev.target.value)} />,
            (this.state.startTimeHour == '' && this.state.startTimeMinute == '' ?
                            <p style={{color: 'orange'}}>You havent set start time(assuming right now)</p>
                            :
                            null),
            <hr />,
            <label htmlFor="input_duration_minute">Expected trip duration in minutes:</label>,
            <input type="text" id="input_duration_minute"  onChange={(ev) => handleChange('tripDuration', ev.target.value)}/>,
            (this.state.tripDuration == '' ?
                                        <p style={{color: 'red'}}>You havent set your trip duration</p>
                                        :
                                        null),
            <hr />,
            <p>Choose accepted modes of transportation: </p>,
            renderTravelModeCheckbox('driving'),
            renderTravelModeCheckbox('bicycling'),
            renderTravelModeCheckbox('transit'),
        ];
    }

    loadConfigFromLocalStorage() {
        let s = JSON.parse(localStorage.getItem('config'));
        this.setState(s)
    }

    saveConfigToLocalStorage() {
        let stateJson = JSON.stringify(this.state);
        localStorage.setItem('config', stateJson);
    }

    render(){
        return <form>
            {this.renderStartEndTripLocationsInput()}
            <p>Choose places of interest: </p>
            <div style={{height: '200px', overflow: 'scroll'}}>
                {SUPPORTED_PLACES_CODES.map(place_code => this.renderCheckbox(place_code))}
            </div>
            <hr />
            <p>Additional keywords for finding places: </p>
            <label htmlFor="input_keywords">Search for places:</label>
            <input type="text" id="input_keywords"  onChange={(ev) => this.setState({placesKeywords: ev.target.value})}/>
            <hr />
            {this.renderFindRoutesButton()}
            <button type="button" onClick={this.saveConfigToLocalStorage.bind(this)}>Save config</button>
            <button type="button" onClick={this.loadConfigFromLocalStorage.bind(this)}>Load config</button>
        </form>;
    }
}