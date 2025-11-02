import { Component } from '@angular/core';
import { GetReport } from '../../models/interface/get-report.interface';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { CommonModule } from '@angular/common';
import { GoogleMap, MapAdvancedMarker } from '@angular/google-maps';
import { OnInit } from '@angular/core';
import { ReportService } from '../../services/report-service';

@Component({
  selector: 'app-report-list-component',
  imports: [TableModule, ButtonModule, CardModule, CommonModule, GoogleMap, MapAdvancedMarker],
  standalone: true,
  templateUrl: './report-list-component.html',
  styleUrl: './report-list-component.css'
})
export class ReportListComponent implements OnInit {

  constructor(private reportService: ReportService) {

  }

  report: GetReport = {
    id: "",
    title: "",
    description: "",
    categoryId: {
      id: null,
      name: ""
    },
    zoneId: {
      id: "",
      name: ""
    },
    reporter: {
      name: "",
      lastName: ""
    },
    state: "",
    severity: "",
    priority: null,
    occurredAt: "",
    createdAt: "",
    updatedAt: "",
    geom: {
      type: "Point",
      coordinates: null,
      point: []
    },
    locationText: ""
  }

  reports: GetReport[] = []

  center: google.maps.LatLngLiteral = { lat: 13.7942, lng: -88.8965 };
  point: google.maps.LatLngLiteral = { lat: 0, lng: 0 };


  zoom = 7;
  mapOptions: google.maps.MapOptions = {
    mapId: 'c23b55e40f719ab3b1055631'
  };

  isActiveWholedata: boolean = false;
  isActiveMap: boolean = false;

  showCompleteReport(reportId: string) {
    this.isActiveWholedata = true;
    this.report = this.reports.find(report => report.id === reportId) || this.report;
    this.formatDate(this.report.occurredAt || '');
  }

  hideCompleteReport(){
    this.isActiveWholedata = false;
  }

  formatDate(occurredAt: string | "") {
    if (!occurredAt) return;

    const date = new Date(occurredAt);

    const year = date.getUTCFullYear();
    const month = String(date.getUTCMonth() + 1).padStart(2, "0");
    const day = String(date.getUTCDate()).padStart(2, "0");
    const hour = String(date.getUTCHours()).padStart(2, "0");
    const minutes = String(date.getUTCMinutes()).padStart(2, "0");
    const seconds = String(date.getUTCSeconds()).padStart(2, "0");

    this.report.occurredAt = `${year}-${month}-${day} ${hour}:${minutes}:${seconds}`;
  }


  showMap(point: number[]) {
    this.point.lng = point[0]
    this.point.lat = point[1]
    this.isActiveMap = true
  }

  hideMap(){
    this.isActiveMap = false
  }

  ngOnInit(): void {
    this.reportService.getReports().subscribe(res => {
      this.reports = res;
    })

  }

}
