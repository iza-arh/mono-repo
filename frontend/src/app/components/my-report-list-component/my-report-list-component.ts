import { Component } from '@angular/core';
import { GetReport } from '../../models/interface/get-report.interface';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { GoogleMap, MapAdvancedMarker } from '@angular/google-maps';


@Component({
  selector: 'app-my-report-list-component',
  imports: [TableModule, ButtonModule, GoogleMap],
  standalone: true,
  templateUrl: './my-report-list-component.html',
  styleUrl: './my-report-list-component.css'
})
export class MyReportListComponent {

  reports: GetReport[] = [
    {
      id: "dc836173-f488-4127-9288-866d52570ca6",
      title: "juan",
      description: "es juan bro ",
      categoryId: {
        id: 11,
        name: "Postes"
      },
      zoneId: {
        id: "7c11fbfc-bccd-46c3-93f4-736710d53a0e",
        name: "Zone 1"
      },
      reporter: {
        name: "Isai",
        lastName: "Hidalgo"
      },
      state: "REPORTED",
      severity: "HIGH",
      priority: 0,
      occurredAt: "2025-11-30T19:08:00Z",
      createdAt: "2025-11-02T02:08:50.124488Z",
      updatedAt: "2025-11-02T02:08:50.124488Z",
      geom: {
        type: "Point",
        coordinates: null,
        point: [
          -88.37001906788198,
          13.64406015594588
        ]
      },
      locationText: "por donde juan"
    },
    {
      id: "2d2352a2-fd51-48ab-9035-88e1143fac68",
      title: "juan",
      description: "es juan bro ",
      categoryId: {
        id: 11,
        name: "Postes"
      },
      zoneId: {
        id: "7c11fbfc-bccd-46c3-93f4-736710d53a0e",
        name: "Zone 1"
      },
      reporter: {
        name: "Isai",
        lastName: "Hidalgo"
      },
      state: "REPORTED",
      severity: "HIGH",
      priority: 0,
      occurredAt: "2025-11-30T19:08:00Z",
      createdAt: "2025-11-02T02:09:50.399703Z",
      updatedAt: "2025-11-02T02:09:50.399703Z",
      geom: {
        type: "Point",
        coordinates: null,
        point: [
          -88.37001906788198,
          13.64406015594588
        ]
      },
      locationText: "por donde juan"
    },
    {
      id: "5619f6df-bac1-406c-bef3-8aca1a6d4f54",
      title: "se me pincho una llanta",
      description: "se me pincho una llanta mi bro",
      categoryId: {
        id: 3,
        name: "Llanta"
      },
      "zoneId": {
        id: "5233ce26-7ca0-4ac9-b266-1c59456cc051",
        name: "Zone 9"
      },
      "reporter": {
        name: "Isai",
        lastName: "Hidalgo"
      },
      state: "REPORTED",
      severity: "LOW",
      priority: 0,
      occurredAt: "2025-11-30T19:11:00Z",
      createdAt: "2025-11-02T02:11:20.634521Z",
      updatedAt: "2025-11-02T02:11:20.634521Z",
      geom: {
        type: "Point",
        coordinates: null,
        point: [
          -89.33421238644289,
          14.107986009231318
        ]
      },
      locationText: "por donde la nina rina"
    }
  ]

  center: google.maps.LatLngLiteral = { lat: 13.7942, lng: -88.8965 };
  point: google.maps.LatLngLiteral = { lat: 0, lng: 0 };


  zoom = 7;
  mapOptions: google.maps.MapOptions = {
    mapId: 'c23b55e40f719ab3b1055631'
  };

}
