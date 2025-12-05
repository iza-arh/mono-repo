import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Category } from '../models/interface/category.interface';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private http = inject(HttpClient);

  private baseUrl = `${environment.apiUrl}/categories`;

  createCategory(categoryData: Category) {
    return this.http.post(this.baseUrl, categoryData);
  }

  getCategories() {
    return this.http.get<Category[]>(this.baseUrl);
  }

  getCategory(id: number) {
    return this.http.get<Category>(`${this.baseUrl}/${id}`);
  }

  partiallyUpdateCategory(id: number, categoryData: Category) {
    return this.http.patch(`${this.baseUrl}/${id}`, categoryData);
  }

  deleteCategory(id: number) {
    return this.http.patch<void>(`${this.baseUrl}/${id}/deactivate`, {});
  }



}
