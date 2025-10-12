import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Category } from '../models/interface/category.interface';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  http = inject(HttpClient);

  createCategory(categoryData: Category) {
    return this.http.post('http://localhost:8080/api/categories', categoryData);
  }

  getCategories() {
    return this.http.get<Category[]>('http://localhost:8080/api/categories');
  }

  getCategory(id: number) {
    return this.http.get<Category>('http://localhost:8080/api/categories/' + id);
  }

  partiallyUpdateCategory(id: number, categoryData: Category) {
    return this.http.patch('http://localhost:8080/api/categories/' + id, categoryData);
  }



}
