(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{14:function(e,t,n){e.exports=n.p+"static/media/logo.5d5d9eef.svg"},15:function(e,t,n){},17:function(e,t,n){"use strict";n.r(t);var o=n(0),a=n.n(o),c=n(7),r=n.n(c),i=n(1),l=n(2),s=n(4),u=n(3),p=n(5),d=(n(14),n(15),window.L),m=function(e){function t(){return Object(i.a)(this,t),Object(s.a)(this,Object(u.a)(t).apply(this,arguments))}return Object(p.a)(t,e),Object(l.a)(t,[{key:"componentDidMount",value:function(){console.log("leaflet component componentdidmount");var e=d.map("leaftlet-map-id",{contextmenu:!0,contextmenuWidth:140,contextmenuItems:[{text:"Show coordinates",callback:function(){console.log("im in callback")}}]}).setView([51.505,-.09],13);d.tileLayer("https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw",{maxZoom:18,attribution:'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery \xa9 <a href="https://www.mapbox.com/">Mapbox</a>',id:"mapbox.streets"}).addTo(e),d.marker([51.5,-.09]).addTo(e).bindPopup("<b>Hello world!</b><br />I am a popup.").openPopup(),d.circle([51.508,-.11],500,{color:"red",fillColor:"#f03",fillOpacity:.5}).addTo(e).bindPopup("I am a circle."),d.polygon([[51.509,-.08],[51.503,-.06],[51.51,-.047]]).addTo(e).bindPopup("I am a polygon.");var t=d.popup();e.on("click",function(n){t.setLatLng(n.latlng).setContent("You clicked the map at "+n.latlng.toString()).openOn(e)})}},{key:"render",value:function(){return a.a.createElement("div",{id:"leaftlet-map-id"},"Hello there, its leaflet component")}}]),t}(o.Component),h=function(e){function t(e){var n;return Object(i.a)(this,t),(n=Object(s.a)(this,Object(u.a)(t).call(this,e))).state={checkedCheckboxes:[]},n}return Object(p.a)(t,e),Object(l.a)(t,[{key:"renderCheckbox",value:function(e){var t=this;return[a.a.createElement("input",{type:"checkbox",id:e,name:e,value:e,onClick:function(n){var o=t.state.checkedCheckboxes.slice();if(n.target.checked)o.push(e);else{var a=o.indexOf(e);o.splice(a,1)}t.setState({checkedCheckboxes:o})}}),a.a.createElement("label",{htmlFor:e},e),a.a.createElement("br",null)]}},{key:"renderFindRoutesButton",value:function(){var e=this;return a.a.createElement("button",{type:"button",onClick:function(){var t={placesOfInterest:e.state.checkedCheckboxes};console.log(t)}},"Ok - find optimal route")}},{key:"render",value:function(){return a.a.createElement("form",null,this.renderCheckbox("restaurants"),this.renderCheckbox("liquor stores"),this.renderCheckbox("museums"),this.renderFindRoutesButton())}}]),t}(o.Component),b=function(e){function t(){return Object(i.a)(this,t),Object(s.a)(this,Object(u.a)(t).apply(this,arguments))}return Object(p.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){return a.a.createElement("div",null,a.a.createElement("h2",null," Title to be done "),a.a.createElement("span",null,a.a.createElement("div",{style:{display:"inline-block",marginRight:"20px"}},a.a.createElement(m,null)),a.a.createElement("div",{style:{display:"inline-block",width:"600px"}},a.a.createElement(h,null))))}}]),t}(o.Component);Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));r.a.render(a.a.createElement(b,null),document.getElementById("root")),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then(function(e){e.unregister()})},8:function(e,t,n){e.exports=n(17)}},[[8,2,1]]]);
//# sourceMappingURL=main.bd50ff33.chunk.js.map