import { Component } from '@angular/core';
import { Zone } from '../../models/interface/zone.interface';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { GoogleMap, MapPolygon } from '@angular/google-maps';
import { OnInit } from '@angular/core';
import { ZoneService } from '../../services/zone-service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-zone-list-component',
  standalone: true,
  imports: [TableModule, ButtonModule, GoogleMap, MapPolygon, CommonModule, RouterLink],
  templateUrl: './zone-list-component.html',
  styleUrl: './zone-list-component.css'
})
export class ZoneListComponent implements OnInit {

  constructor(private zoneService: ZoneService) {
  }

  isActive: boolean = false;

  vertices: google.maps.LatLngLiteral[] = [];

  zones: Zone[] = []

  center: google.maps.LatLngLiteral = { lat: 13.7942, lng: -88.8965 };
  zoom = 7;

  showPolygon(index: number) {
    this.isActive = true;
    this.vertices = this.zones[index].geom.coordinates[0].map(v => ({ lat: v[0], lng: v[1] }))
  }

  hidePolygon() {
    this.isActive = false;
  }

  ngOnInit(): void {
    this.zoneService.getZones().subscribe((response) => {
      this.zones = response;
    })
  }

  deleteZone(id: string) {
    this.zoneService.deleteZone(id).subscribe(() => {
      this.zoneService.getZones().subscribe((response) => {
        this.zones = response;
      });
    })
  }
}
