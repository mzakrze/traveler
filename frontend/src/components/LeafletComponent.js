import React, { Component } from 'react';


// Leaflet sources from CDN
import './LeafletComponent.css';
const L = window.L;

var polyUtil = require('polyline-encoded');

export default class LeafletComponent extends Component {

    mymap: any;
    startLocationPopup: any;
    endLocationPopup: any;

    constructor(props) {
        super(props);
        this.startLocationPopup = null;
        this.endLocationPopup = null;
        this.state = { };
    }

    componentWillReceiveProps(nextProps) {

        const popupOptions = {
            closeOnClick: false,
            autoClose: false
        };

        if(this.props.startLocation !== nextProps.startLocation) {
            if(this.startLocationPopup != null) {
                this.mymap.removeLayer(this.startLocationPopup)
            }
            this.startLocationPopup = new L.popup(popupOptions)
                    .setLatLng(nextProps.startLocation)
                    .setContent('Start location');
            this.startLocationPopup.openOn(this.mymap);
        }

        if(this.props.endLocation !== nextProps.endLocation) {
            if(this.endLocationPopup != null) {
                this.mymap.removeLayer(this.endLocationPopup)
            }
            this.endLocationPopup = new L.popup(popupOptions)
                    .setLatLng(nextProps.endLocation)
                    .setContent('End location');
            this.endLocationPopup.openOn(this.mymap);
        }


        if(this.props.findRouteResultRev != nextProps.findRouteResultRev) {

            this.plotFindRoutesResponse(nextProps.findRouteResult);

        }
    }

    plotFindRoutesResponse(result) {

        let randomColor = () => {
            const possible = "0123456789abcdef";
            let r = () => possible[Math.floor(Math.random() * 16)];
            return "#" + r() + r() + r();
        }

        let route = result.directions.routes[0];

        for(let leg of route.legs) {

            let legColor = randomColor();

            for(let s of leg.steps) {
                var polyline = polyUtil.decode(s.polyline.points, 5);

                var leaftletPolyline = new L.polyline(polyline, {color: legColor});

                this.mymap.addLayer(leaftletPolyline);

                leaftletPolyline.bindTooltip("TODO Polyline  tooltip content: " + legColor);
            }
        }
    }


    componentDidMount(){
//        this.mymap = L.map('leaftlet-map-id').setView([52.218994864793, 21.011712029573467], 17); // coordsy elki
        this.mymap = L.map('leaftlet-map-id').setView([51.505, -0.09], 13);

        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 18,
            attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
                '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
                'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
            id: 'mapbox.streets'
        }).addTo(this.mymap);

        let onMapClick = (e) => {
            this.props.handleClickOnMap(e.latlng);
        }

        this.mymap.on('click', onMapClick);
    }


    render() {
        return <div id="leaftlet-map-id" />;
    }
}

