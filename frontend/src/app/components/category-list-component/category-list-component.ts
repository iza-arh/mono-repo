import { Component } from '@angular/core';
import { TableModule } from 'primeng/table';
import { Category } from '../../models/interface/category.interface';
import { OnInit } from '@angular/core';
import { CategoryService } from '../../services/category-service';
import { ButtonModule } from 'primeng/button';
import { RouterLink } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';

@Component({
  selector: 'app-category-list-component',
  standalone: true,
  imports: [TableModule, ButtonModule, RouterLink, Toast],
  templateUrl: './category-list-component.html',
  styleUrl: './category-list-component.css',
  providers: [MessageService]
})
export class CategoryListComponent implements OnInit {

  constructor(private categoryService: CategoryService, private messageService: MessageService) { }

  categories: Category[] = []

  ngOnInit(): void {
    this.categoryService.getCategories().subscribe((response) => {
      this.categories = response;
    })
  }

  showSuccessToast(message: string) {
    this.messageService.add({ severity: 'success', summary: 'Success', detail: message, life: 3000 });
  }

  deleteCategory(id: number) {
    this.categoryService.deleteCategory(id).subscribe(() => {
      this.categoryService.getCategories().subscribe({
        next: (response) => {
          this.categories = response;
          this.showSuccessToast("Category was deleted")
        }
      })
    })
  }
}
