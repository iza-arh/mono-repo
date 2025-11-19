import { Component } from '@angular/core';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule, NgForm } from '@angular/forms';
import { FloatLabel } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { Category } from '../../models/interface/category.interface';
import { CommonModule } from '@angular/common';
import { CategoryService } from '../../services/category-service';
import { ActivatedRoute } from '@angular/router';
import { OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';

@Component({
  selector: 'app-categories-form-component',
  imports: [CardModule, InputTextModule, FormsModule, FloatLabel, ButtonModule, CommonModule, Toast],
  templateUrl: './categories-form-component.html',
  styleUrl: './categories-form-component.css',
  providers: [MessageService]
})
export class CategoriesFormComponent implements OnInit {

  constructor(private categoryService: CategoryService, private route: ActivatedRoute, private messageService: MessageService) {
  }

  category: Category = {
    id: null,
    name: "",
    code: ""
  }

  showErrorToast(message: string) {
    this.messageService.add({ severity: 'error', summary: 'Error', detail: message, life: 3000 });
  }

  showSuccessToast(message: string) {
    this.messageService.add({ severity: 'success', summary: 'Success', detail: message, life: 3000 });
  }

  cleanForm(Form: NgForm) {
    this.category.id = null;
    this.category.name = "";
    this.category.code = "";
    Form.resetForm(this.category)
  }

  createOrUpdateCategory(formvalue: Category, Form: NgForm) {
    if (this.category.id !== null) {
      this.categoryService.partiallyUpdateCategory(this.category.id, formvalue).subscribe({
        next: (response) => {
          console.log(response);
          this.cleanForm(Form);
          this.showSuccessToast('Successfully updated');
        },
        error: (err) => {
          this.showErrorToast(err.error.message);
        }
      }
      )
    } else {
      })
    }
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      if (Number(params.get('id')) !== 0) {
        this.categoryService.getCategory(Number(params.get('id'))).subscribe((response) => {
          this.category = {
            id: response.id,
            name: response.name,
            code: response.code
          }
        })
      }
    })
  }

}
