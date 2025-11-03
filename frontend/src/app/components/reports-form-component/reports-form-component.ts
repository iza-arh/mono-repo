import { Component, ViewChild } from '@angular/core';
import { CardModule } from 'primeng/card';
import { FileUpload } from 'primeng/fileupload';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { FormsModule, NgForm } from '@angular/forms';
import { ReportInterface } from '../../models/interface/report.interface';
import { FloatLabel } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { CommonModule } from '@angular/common';
import { TextareaModule } from 'primeng/textarea';
import { CategoryService } from '../../services/category-service';
import { Category } from '../../models/interface/category.interface';
import { OnInit } from '@angular/core';
import { Select } from 'primeng/select';
import { Zone } from '../../models/interface/zone.interface';
import { ZoneService } from '../../services/zone-service';
import { GoogleMap, MapAdvancedMarker } from '@angular/google-maps';
import { HttpClient } from '@angular/common/http';
import { DatePickerModule } from 'primeng/datepicker';
import { ReportService } from '../../services/report-service';
import { AuthService } from '@auth0/auth0-angular';


@Component({
  selector: 'app-reports-form-component',
  imports: [CommonModule, CardModule, FileUpload, ToastModule, ButtonModule, FormsModule, FloatLabel, InputTextModule, TextareaModule, Select, GoogleMap, MapAdvancedMarker, DatePickerModule],
  standalone: true,
  templateUrl: './reports-form-component.html',
  styleUrl: './reports-form-component.css',
  providers: [MessageService]
})
export class ReportsFormComponent implements OnInit {


  constructor(private categoryService: CategoryService, private zoneService: ZoneService, private http: HttpClient, private reportService: ReportService, private auth: AuthService) {

  }

  report: ReportInterface = {
    id: '',
    title: '',
    description: '',
    categoryId: '',
    zoneId: '',
    severity: '',
    reporterId: '',
    state: '',
    priority: null,
    occurredAt: null,
    locationText: '',
    geom: {
      type: "Point",
      point: []
    }
  }

  date!: Date;
  time!: Date;

  categories: Category[] = [];

  zones: Zone[] = [];

  severity: string[] = ['LOW', 'MEDIUM', 'HIGH']

  state: string[] = ['REPORTED', 'VALIDATED', 'REJECTED', 'ASSIGNED', 'IN_TRANSIT', 'IN_PROGRESS', 'RESOLVED', 'NO_PROCEDE'
  ]


  @ViewChild('fu') fileUpload!: FileUpload;
  selectedFile: File | null = null;;

  center: google.maps.LatLngLiteral = { lat: 13.7942, lng: -88.8965 };
  point: google.maps.LatLngLiteral = { lat: 0, lng: 0 };


  zoom = 7;
  mapOptions: google.maps.MapOptions = {
    mapId: 'c23b55e40f719ab3b1055631'
  };

  isActive: boolean = false;

  showMap() {
    this.isActive = !this.isActive;
  }



  obtenerUbicacion() {
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          this.point.lat = position.coords.latitude;
          this.point.lng = position.coords.longitude;
        },
        (error) => {
          console.error('Error al obtener la ubicación:', error.message);
        }
      );
    } else {
      console.error('Geolocalización no soportada en este navegador.');
    }
  }

  addPoint(event: google.maps.MapMouseEvent) {
    if (event.latLng) {
      this.point = event.latLng.toJSON()
      this.report.geom.point[0] = this.point.lng
      this.report.geom.point[1] = this.point.lat
    }
  }

  onFileSelect(event: any) {
    if (event.files && event.files.length > 0) {
      this.selectedFile = event.files[0];
    }
  }

  formattedDate() {
    if (!this.date) return '';
    const year = this.date.getFullYear();
    const month = String(this.date.getMonth() + 1).padStart(2, '0');
    const day = String(this.date.getDate()).padStart(2, '0');
    const hour = String(this.time.getHours()).padStart(2, '0');
    const minutes = String(this.time.getMinutes()).padStart(2, '0');
    const seconds = String(this.time.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day}T${hour}:${minutes}:${seconds}Z`;
  }


  clearForm(form: NgForm) {
    this.report.id = null;
    this.report.title = '';
    this.report.description = '';
    this.report.categoryId = '';
    this.report.zoneId = '';
    this.report.severity = '';
    this.report.reporterId = '';
    this.report.state = '';
    this.report.priority = null;
    this.report.occurredAt = null;
    this.report.locationText = '';
    form.resetForm(this.report)

    this.selectedFile = null;
    if (this.fileUpload) {
      this.fileUpload.clear();
    }
  }

  createReport(formValue: ReportInterface, form: NgForm) {
    const formData = new FormData();
    if (this.selectedFile) {
      formData.append('photo', this.selectedFile);
    }
    this.report.title = formValue.title;
    this.report.description = formValue.description;
    this.report.categoryId = formValue.categoryId;
    this.report.zoneId = formValue.zoneId;
    this.report.severity = formValue.severity;
    this.report.priority = formValue.priority;
    this.report.occurredAt = formValue.occurredAt;
    this.report.locationText = formValue.locationText;
    this.report.occurredAt = this.formattedDate();
    if (this.report.reporterId !== null && this.report.reporterId !== '') {
      this.reportService.createReport(this.report).subscribe(res => {
        this.clearForm(form);
      })
    }
  }

  ngOnInit(): void {
    this.categoryService.getCategories().subscribe((res) => {
      this.categories = res;
    })
    this.zoneService.getZones().subscribe((res) => {
      this.zones = res;
    })
    this.auth.user$.subscribe(user => {
      this.report.reporterId = user?.sub || '';
    })
    this.obtenerUbicacion()
  }
}
