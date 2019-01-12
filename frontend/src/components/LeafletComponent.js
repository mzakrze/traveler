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
        let colorIndex = 0;
        for(let directions of result.directions) {

            let route = directions.routes[0];

            for(let leg of route.legs) {

                let legColor = this.props.getColor(colorIndex);
                colorIndex++;

                for(let s of leg.steps) {
                    var polyline = polyUtil.decode(s.polyline.points, 5);

                    var leaftletPolyline = new L.polyline(polyline, {color: legColor});

                    this.mymap.addLayer(leaftletPolyline);

                    // leaftletPolyline.bindTooltip("TODO Polyline  tooltip content: " + legColor);
                }
            }

        }



        // -----------------------------------------------------

        let places = result.places;

        console.log(places)
        console.log(result.selectedPlaces.placesToVisitInOrder.map(e => e.id))

        for(let r of places.results) {

            let ord = result.selectedPlaces.placesToVisitInOrder;
            if(result.selectedPlaces.placesToVisitInOrder.map(e => e.id).includes(r.place_id) == false) {
                continue;
            }


            let latlng = r.geometry.location;
            let proposedTime = result.selectedPlaces.placesToVisitInOrder.find(e => e.id == r.place_id).proposedTime;
            let name = "<p><b>" + r.name + "</b> " + proposedTime + " minutes (" + r.types.join() + ")" + (r.rating != null ? "\nrating:" + r.rating : "") + "</p>";

            let p = new L.popup({
                    closeOnClick: false,
                    autoClose: false
                })
                .setLatLng(latlng)
                .setContent(name);

            p.openOn(this.mymap);
        }


        // -----------------------------------------------------
        const popupOptions = {
            closeOnClick: false,
            autoClose: false
        };
        this.startLocationPopup = new L.popup(popupOptions)
                    .setLatLng(result.startLocation)
                    .setContent('Start location');
        this.startLocationPopup.openOn(this.mymap);

        this.endLocationPopup = new L.popup(popupOptions)
                    .setLatLng(result.endLocation)
                    .setContent('End location');
        this.endLocationPopup.openOn(this.mymap);
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

