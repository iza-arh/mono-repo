import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Zone } from '../models/interface/zone.interface';

@Injectable({
  providedIn: 'root'
})
export class ZoneService {

  constructor(private http: HttpClient) {
  }

  createZone(zoneData: Zone) {
    return this.http.post('http://localhost:8080/api/zones', zoneData)
  }

  getZones() {
    return this.http.get<Zone[]>('http://localhost:8080/api/zones')
  }

  getZone(id: string | null) {
    return this.http.get<Zone>('http://localhost:8080/api/zones/' + id)
  }

  partiallyUpdateZone(id: string | null, zoneData: Zone) {
    return this.http.patch('http://localhost:8080/api/zones/' + id, zoneData)
  }

  deleteZone(id: string | null) {
    return this.http.patch<void>('http://localhost:8080/api/zones/' + id + '/deactivate', {})
  }

}
