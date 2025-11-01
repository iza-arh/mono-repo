import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ReportInterface } from '../models/interface/report.interface';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  constructor(private http: HttpClient) {
  }

  createReport(reportData: ReportInterface) {
    return this.http.post<ReportInterface>('http://localhost:8080/api/reports', reportData)
  }

}
