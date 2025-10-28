import { Component } from '@angular/core';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule, NgForm } from '@angular/forms';
import { FloatLabel } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserInterface } from '../../models/interface/user.interface';
import { Select } from 'primeng/select';

@Component({
  selector: 'app-user-form-component',
  standalone: true,
  imports: [CardModule, FormsModule, FloatLabel, InputTextModule, CommonModule, ButtonModule, Select],
  templateUrl: './user-form-component.html',
  styleUrl: './user-form-component.css'
})
export class UserFormComponent implements OnInit {

  constructor(private root: ActivatedRoute) {

  }

  roles: string[] = ['CITIZEN', 'TECHNICIAN', 'MANAGER', 'ADMIN']

  user: UserInterface = {
    id: null,
    email: 'rh23003@ues.edu.sv',
    name: 'Isai',
    lastName: 'Alberto',
    role: '',
    phone: ''
  }

  clearForm(Form: NgForm) {
    this.user.id = null;
    this.user.name = '';
    this.user.lastName = '';
    this.user.role = '';
    this.user.phone = '';
    this.user.email = '';

    Form.resetForm(this.user);
  }

  updateUser(formValues: UserInterface, Form: NgForm) {
    this.user.role = formValues.role;
    this.user.phone = formValues.phone;
    this.clearForm(Form)
  }

  ngOnInit(): void {
    this.root.paramMap.subscribe((params) => {
      console.log(params.get('id'))
    })
  }

}
