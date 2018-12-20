/* @flow */
import React, { Component } from 'react';

export default class FormComponent extends Component {
    props: Props;
    state: State;

    constructor(props: Props){
        super(props);
        this.state = {
            checkedCheckboxes: []
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
            })
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
                placesOfInterest: this.state.checkedCheckboxes
            }
            // FIXME
            console.log(data);
        }
        return <button type="button" onClick={send}>Ok - find optimal route</button>
    }

    render(){
        return <form>
            {this.renderCheckbox('restaurants')}
            {this.renderCheckbox('liquor stores')}
            {this.renderCheckbox('museums')}
            {this.renderFindRoutesButton()}
        </form>;
    }

}