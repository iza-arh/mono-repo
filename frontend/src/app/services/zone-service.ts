import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Zone } from '../models/interface/zone.interface';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ZoneService {

  constructor(private http: HttpClient) {}

  private baseUrl = `${environment.apiUrl}/zones`;

  createZone(zoneData: Zone) {
    return this.http.post(this.baseUrl, zoneData);
  }

  getZones() {
    return this.http.get<Zone[]>(this.baseUrl);
  }

  getZone(id: string | null) {
    return this.http.get<Zone>(`${this.baseUrl}/${id}`);
  }

  partiallyUpdateZone(id: string | null, zoneData: Zone) {
    return this.http.patch(`${this.baseUrl}/${id}`, zoneData);
  }

  deleteZone(id: string | null) {
    return this.http.patch<void>(`${this.baseUrl}/${id}/deactivate`, {});
  }

}
