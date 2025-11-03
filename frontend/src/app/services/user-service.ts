import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserInterface } from '../models/interface/user.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {
  }

  createUser(userData: UserInterface) {
    return this.http.post('http://localhost:8080/api/users', userData);
  }

  getUsers() {
    return this.http.get<UserInterface[]>('http://localhost:8080/api/users');
  }

  getUser(id: string) {
    const encodedUserId = encodeURIComponent(id);
    const url = `http://localhost:8080/api/users/${encodedUserId}`;
    return this.http.get<UserInterface>(url);
  }

  updateUserRole(id: string, userData: UserInterface) {
    const encodedUserId = encodeURIComponent(id);
    const url = `http://localhost:8080/api/users/${encodedUserId}` + `/role`;
    return this.http.patch(url, userData);
  }

  updateUserPhoneNumber(id: string, userData: UserInterface) {
    const encodedUserId = encodeURIComponent(id);
    const url = `http://localhost:8080/api/users/${encodedUserId}` + `/phone`;
    return this.http.patch(url, userData);
  }

}
