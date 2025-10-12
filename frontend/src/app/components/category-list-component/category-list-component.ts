import { Component } from '@angular/core';
import { TableModule } from 'primeng/table';
import { Category } from '../../models/interface/category.interface';
import { OnInit } from '@angular/core';
import { CategoryService } from '../../services/category-service';
import { ButtonModule } from 'primeng/button';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-category-list-component',
  standalone: true,
  imports: [TableModule, ButtonModule, RouterLink],
  templateUrl: './category-list-component.html',
  styleUrl: './category-list-component.css'
})
export class CategoryListComponent implements OnInit {

  constructor(private categoryService: CategoryService) { }

  categories: Category[] = []

  ngOnInit(): void {
    this.categoryService.getCategories().subscribe((response) => {
      this.categories = response;
    })
  }
}
