import { Component, ViewChild } from '@angular/core';
import { CardModule } from 'primeng/card';
import { FileUpload } from 'primeng/fileupload';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { FormsModule, NgForm } from '@angular/forms';
import { Report } from '../../models/interface/report.interface';
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

@Component({
  selector: 'app-reports-form-component',
  imports: [CommonModule, CardModule, FileUpload, ToastModule, ButtonModule, FormsModule, FloatLabel, InputTextModule, TextareaModule, Select],
  standalone: true,
  templateUrl: './reports-form-component.html',
  styleUrl: './reports-form-component.css',
  providers: [MessageService]
})
export class ReportsFormComponent implements OnInit {


  constructor(private categoryService: CategoryService, private zoneService: ZoneService) {

  }

  report: Report = {
    id: '',
    title: '',
    description: '',
    categoryId: '',
    zoneId: '',
    severity: '',
    UserId: '',
    state: '',
    priority: null,
    occurredAt: null,
    locationText: ''
  }

  categories: Category[] = [];

  zones: Zone[] = [];

  severity: string[] = ['LOW', 'MEDIUM', 'HIGH']

  state: string[] = ['REPORTED', 'VALIDATED', 'REJECTED', 'ASSIGNED', 'IN_TRANSIT', 'IN_PROGRESS', 'RESOLVED', 'NO_PROCEDE'
  ]


  @ViewChild('fu') fileUpload!: FileUpload;
  selectedFile: File | null = null;;


  onFileSelect(event: any) {
    if (event.files && event.files.length > 0) {
      this.selectedFile = event.files[0];
    }
  }

  clearForm(form: NgForm) {
    this.report.id = '';
    this.report.title = '';
    this.report.description = '';
    this.report.categoryId = '';
    this.report.zoneId = '';
    this.report.severity = '';
    this.report.UserId = '';
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

  createReport(formValue: Report, form: NgForm) {
    const now = new Date();
    const formData = new FormData();
    this.report.occurredAt = now;
    if (this.selectedFile) {
      formData.append('photo', this.selectedFile);
    }
    Object.entries(formValue).forEach(([key, value]) => {
      formData.append(key, value);
    });
    this.clearForm(form)
  }


  ngOnInit(): void {
    this.categoryService.getCategories().subscribe((res) => {
      this.categories = res;
    })
    this.zoneService.getZones().subscribe((res) => {
      this.zones = res;
      console.log(this.zones)
    })
  }
}
