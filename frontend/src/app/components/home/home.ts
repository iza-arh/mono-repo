import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '@auth0/auth0-angular';
import { OnInit } from '@angular/core';
import { UserInterface } from '../../models/interface/user.interface';
import { UserService } from '../../services/user-service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent implements OnInit {

  constructor(private auth: AuthService, private userService: UserService) { }

  myUser: UserInterface = {
    id: "",
    name: "",
    lastName: "",
    email: "",
    phone: null,
    role: ""
  }

  ngOnInit(): void {
    this.auth.user$.subscribe(user => {
      console.log(user)
      this.myUser.id = user?.sub || "";
      this.myUser.name = user?.given_name || "";
      this.myUser.lastName = user?.family_name || "";
      this.myUser.email = user?.email || "";
      this.userService.createUser(this.myUser).subscribe(res => {
      });
    });
  }


}