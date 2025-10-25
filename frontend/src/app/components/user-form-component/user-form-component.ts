import { Component } from '@angular/core';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule, NgForm } from '@angular/forms';
import { FloatLabel } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../models/interface/user.interface';

@Component({
  selector: 'app-user-form-component',
  standalone: true,
  imports: [CardModule, FormsModule, FloatLabel, InputTextModule, CommonModule, ButtonModule],
  templateUrl: './user-form-component.html',
  styleUrl: './user-form-component.css'
})
export class UserFormComponent implements OnInit {

  constructor(private root: ActivatedRoute) {

  }

  user: User = {
    id: null,
    email: '',
    name: '',
    lastName: '',
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

  updateUser(FormValues: User, Form: NgForm) {
      this.clearForm(Form)
  }

  ngOnInit(): void {
    this.root.paramMap.subscribe((params) => {
      console.log(params.get('id'))
    })
  }

}
