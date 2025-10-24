import { Component } from '@angular/core';
import { GoogleMap, MapPolygon, MapAdvancedMarker } from '@angular/google-maps';
import { Zone } from '../../models/interface/zone.interface';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule, NgForm } from '@angular/forms';
import { FloatLabel } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { ZoneService } from '../../services/zone-service';
import { OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-zone-form-component',
  standalone: true,
  imports: [GoogleMap, MapPolygon, InputTextModule, FormsModule, FloatLabel, CardModule, CommonModule, ButtonModule, MapAdvancedMarker],
  templateUrl: './zone-form-component.html',
  styleUrl: './zone-form-component.css'
})
export class ZoneFormComponent implements OnInit {

  constructor(private zoneService: ZoneService, private route: ActivatedRoute) {
  }

  vertices: google.maps.LatLngLiteral[] = [];


  myZone: Zone = {
    id: null,
    name: "",
    geom: {
      type: "Polygon",
      coordinates: [[]]
    }
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((param) => {
      if (param.get('id') !== null) {
        this.zoneService.getZone(param.get('id')).subscribe((resonse) => {
          this.myZone = resonse;
          this.myZone.geom.coordinates[0].pop();
          this.vertices = this.myZone.geom.coordinates[0].map(v => ({ lat: v[0], lng: v[1] }))
        })
      }
    })
  }

  center: google.maps.LatLngLiteral = { lat: 13.7942, lng: -88.8965 };
  zoom = 7;

  mapOptions: google.maps.MapOptions = {
    mapId: 'c23b55e40f719ab3b1055631'
  };

  isActive: boolean = false;

  showMap() {
    this.isActive = true;
  }

  addVertex(event: google.maps.MapMouseEvent) {
    if (event.latLng) {
      const vertex = event.latLng.toJSON();
      this.vertices = [...this.vertices, vertex];
      this.myZone.geom.coordinates[0] = this.vertices.map(v => [v.lat, v.lng])
    }
  }

  removeVertex(index: number) {
    this.vertices.splice(index, 1);
    this.myZone.geom.coordinates[0] = this.vertices.map(v => [v.lat, v.lng]);
  }

  hideMap() {
    const actualVertices = this.myZone.geom.coordinates[0]
    if (actualVertices.length >= 3) {
      actualVertices.push(actualVertices[0])
      this.isActive = false;
    }
  }

  clearForm(Form: NgForm) {
    this.myZone.name = "";
    this.myZone.geom.coordinates = [[]];
    Form.resetForm()
  }

  createOrUpdateZone(FormValue: any, Form: NgForm) {
    this.myZone.name = FormValue.name;
    if (this.myZone.id === null) {
      this.zoneService.createZone(this.myZone).subscribe((response) => {
        console.log(response)
        this.clearForm(Form)
      })
    } else {
      this.zoneService.partiallyUpdateZone(this.myZone.id, this.myZone).subscribe((response) => {
        console.log(response)
      })
    }
  }
}
