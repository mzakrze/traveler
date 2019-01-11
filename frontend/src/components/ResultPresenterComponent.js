/* @flow */
import React, { Component } from 'react';

type Props = {
    notifyChangeScreen: Function,
    result: FindRouteResult,
}

export default class ResultPresenterComponent extends Component {

    renderSingleStep(name, color, singleStep) {
        let result = [];
        result.push(<p style={{'background-color': color}}>{name}</p>);

        let route = singleStep.routes[0];

        let summary = route.summery;
        for(let leg of route.legs) {
            let letRes = "   " + leg.distance.text + "(" + leg.duration.text + ")";
            result.push(<p
                onMouseEnter={() => console.log('xd')}
                onMouseLeave={() => console.log('xd')} >
                {letRes}
            </p>);
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
                colorIndex++;
                steps.push(this.renderSingleStep(previousStepName + "->" + currentStepName, color, step));
            }
        }

        return (<div>
            Hello it is result presenter
            {steps}
            <button type="button" onClick={this.props.notifyChangeScreen}>Search again</button>
        </div>);
    }

}