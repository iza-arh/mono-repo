import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserInterface } from '../models/interface/user.interface';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {}

  private baseUrl = `${environment.apiUrl}/users`;

  createUser(userData: UserInterface) {
    return this.http.post(this.baseUrl, userData);
  }

  getUsers() {
    return this.http.get<UserInterface[]>(this.baseUrl);
  }

  getUser(id: string) {
    const encodedUserId = encodeURIComponent(id);
    const url = `${this.baseUrl}/${encodedUserId}`;
    return this.http.get<UserInterface>(url);
  }

  updateUserRole(id: string, userData: UserInterface) {
    const encodedUserId = encodeURIComponent(id);
    const url = `${this.baseUrl}/${encodedUserId}/role`;
    return this.http.patch(url, userData);
  }

  updateUserPhoneNumber(id: string, userData: UserInterface) {
    const encodedUserId = encodeURIComponent(id);
    const url = `${this.baseUrl}/${encodedUserId}/phone`;
    return this.http.patch(url, userData);
  }

}
