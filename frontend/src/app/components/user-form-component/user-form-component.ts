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
import { UserService } from '../../services/user-service';

@Component({
  selector: 'app-user-form-component',
  standalone: true,
  imports: [CardModule, FormsModule, FloatLabel, InputTextModule, CommonModule, ButtonModule, Select],
  templateUrl: './user-form-component.html',
  styleUrl: './user-form-component.css'
})
export class UserFormComponent implements OnInit {

  constructor(private root: ActivatedRoute, private userService: UserService) {

  }

  roles: string[] = ['CITIZEN', 'TECHNICIAN', 'MANAGER', 'ADMIN']

  myUser: UserInterface = {
    id: null,
    email: 'rh23003@ues.edu.sv',
    name: 'Isai',
    lastName: 'Alberto',
    role: '',
    phone: ''
  }

  updateUser(formValues: UserInterface, Form: NgForm) {
    this.myUser.role = formValues.role;
    this.myUser.phone = formValues.phone;
    this.userService.updateUserRole(this.myUser.id || "", this.myUser).subscribe(res => {
    })
    this.userService.updateUserPhoneNumber(this.myUser.id || "", this.myUser).subscribe(res => {
    })
  }

  ngOnInit(): void {
    this.root.paramMap.subscribe((params) => {
      this.userService.getUser(params.get('id') || "").subscribe(res => {
        this.myUser = res;
      })
    })
  }

}
