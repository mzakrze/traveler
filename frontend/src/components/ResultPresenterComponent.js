/* @flow */
import React, { Component } from 'react';

type Props = {
    notifyChangeScreen: Function,
    result: FindRouteResult,
}

export default class ResultPresenterComponent extends Component {

    constructor(props){
        super(props)

        this.state = {currentlyExpandedRouteIndex: null}
    }

    renderSingleStep(name, color, singleStep, routeIndex) {
        let result = [];
        result.push(<p
                style={{'background-color': color}}
                onMouseEnter={() => this.setState({currentlyExpandedRouteIndex: routeIndex})}
                onMouseLeave={() => this.setState({currentlyExpandedRouteIndex: null})} >
            {name}
        </p>);

        let route = singleStep.routes[0];

        let summary = route.summery;
        for(let leg of route.legs) {
            let letRes = "   " + leg.distance.text + "(" + leg.duration.text + ")";
            result.push(<p>
                {letRes}
            </p>);
            let instructions = "";
            for(let step of leg.steps) {
                instructions += step.html_instructions + "<br />";
            }
            if(routeIndex == this.state.currentlyExpandedRouteIndex) {
                result.push(<div dangerouslySetInnerHTML={{ __html: instructions }} />)
            }
        }

        return <div>{[].concat(result)}</div>;
    }

    render() {
        let placeIdToName = (id) => {
            return this.props.result.places.results.filter(e => e.place_id == id)[0].name;
        }

        let steps = [];
        if(this.props.result != null) {
            let colorIndex = 0;
            let routeIndex = 0;
            for(let i = 0; i < this.props.result.directions.length; i++) {
                let step = this.props.result.directions[i];
                let previousStepName = null, currentStepName = null;
                if(i == 0) {
                    previousStepName = "Start location";
                } else {
                    previousStepName = placeIdToName(this.props.result.selectedPlaces.placesToVisitInOrder[i - 1].id);
                }
                if(i == this.props.result.directions.length - 1) {
                    currentStepName = "End location";
                } else {
                    currentStepName = placeIdToName(this.props.result.selectedPlaces.placesToVisitInOrder[i].id);
                }
                let color = this.props.getColor(colorIndex);
                steps.push(this.renderSingleStep(previousStepName + "->" + currentStepName, color, step, routeIndex));
                colorIndex++;
                routeIndex++;
            }
        }

        return (<div style={{position: 'fixed', top: 0}}>
            {steps}
            <button type="button" onClick={() => window.location = "/"}>Search again</button>
        </div>);
    }

}