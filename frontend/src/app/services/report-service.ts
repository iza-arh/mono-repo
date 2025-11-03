import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ReportInterface } from '../models/interface/report.interface';
import { GetReport } from '../models/interface/get-report.interface';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  constructor(private http: HttpClient) {
  }

  createReport(reportData: ReportInterface) {
    return this.http.post<ReportInterface>('http://localhost:8080/api/reports', reportData)
  }

  getReports(){
    return this.http.get<GetReport[]>('http://localhost:8080/api/reports');
  }

  getUserReports(userId: string){
    const encodedUserId = encodeURIComponent(userId);
    const url = `http://localhost:8080/api/reports/reporter/${encodedUserId}`;
        return this.http.get<GetReport[]>(url);
  }

}
