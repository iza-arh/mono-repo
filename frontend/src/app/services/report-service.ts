import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ReportInterface } from '../models/interface/report.interface';
import { GetReport } from '../models/interface/get-report.interface';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  constructor(private http: HttpClient) {}

  private baseUrl = `${environment.apiUrl}/reports`;

  createReport(reportData: ReportInterface) {
    return this.http.post<ReportInterface>(this.baseUrl, reportData)
  }

  getReports(){
    return this.http.get<GetReport[]>(this.baseUrl);
  }

  getUserReports(userId: string){
    const encodedUserId = encodeURIComponent(userId);
    const url = `${this.baseUrl}/reporter/${encodedUserId}`;
        return this.http.get<GetReport[]>(url);
  }

  getReport(reportId: string){
    return this.http.get<GetReport>(`${this.baseUrl}/${reportId}`);
  }

}
