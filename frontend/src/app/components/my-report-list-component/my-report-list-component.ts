import { Component } from '@angular/core';
import { GetReport } from '../../models/interface/get-report.interface';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { GoogleMap, MapAdvancedMarker } from '@angular/google-maps';
import { CommonModule } from '@angular/common';
import { ReportService } from '../../services/report-service';
import { OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';


@Component({
  selector: 'app-my-report-list-component',
  imports: [TableModule, ButtonModule, GoogleMap, MapAdvancedMarker, CommonModule],
  standalone: true,
  templateUrl: './my-report-list-component.html',
  styleUrl: './my-report-list-component.css'
})
export class MyReportListComponent implements OnInit {

  constructor(private reportService: ReportService, private auth: AuthService) {
  }


  reports: GetReport[] = []

  isActiveMap: boolean = false;

  isActiveWholedata: boolean = false;

  center: google.maps.LatLngLiteral = { lat: 13.7942, lng: -88.8965 };
  point: google.maps.LatLngLiteral = { lat: 0, lng: 0 };


  zoom = 7;
  mapOptions: google.maps.MapOptions = {
    mapId: 'c23b55e40f719ab3b1055631'
  };

  showMap(point: number[]) {
    this.point.lng = point[0]
    this.point.lat = point[1]
    this.isActiveMap = true;
  }

  hideMap() {
    this.isActiveMap = false;
  }

  ngOnInit(): void {

    this.auth.user$.subscribe(user => {
      this.reportService.getUserReports(user?.sub || '').subscribe(res => {
        this.reports = res;
      })
    })
  }

}
