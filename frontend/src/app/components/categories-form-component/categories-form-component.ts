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

@Component({
  selector: 'app-categories-form-component',
  imports: [CardModule, InputTextModule, FormsModule, FloatLabel, ButtonModule, CommonModule],
  templateUrl: './categories-form-component.html',
  styleUrl: './categories-form-component.css'
})
export class CategoriesFormComponent implements OnInit {

  constructor(private categoryService: CategoryService, private route: ActivatedRoute) {
  }

  category: Category = {
    id: null,
    name: "",
    code: ""
  }

  cleanForm(Form: NgForm){
    this.category.id = null;
    this.category.name = "";
    this.category.code = "";
    Form.resetForm(this.category)
  }

  createOrUpdateCategory(formvalue: Category, Form: NgForm) {
    if (this.category.id !== null) {
      this.categoryService.partiallyUpdateCategory(this.category.id, formvalue).subscribe((response) => {
        console.log(response)
        this.cleanForm(Form);
      })
    } else {
      this.categoryService.createCategory(formvalue).subscribe((response) => {
        console.log(response)
        this.cleanForm(Form)
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
