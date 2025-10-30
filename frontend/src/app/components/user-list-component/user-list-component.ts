import { Component } from '@angular/core';
import { UserInterface } from '../../models/interface/user.interface';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { RouterLink } from '@angular/router';
import { OnInit } from '@angular/core';
import { UserService } from '../../services/user-service';

@Component({
  selector: 'app-user-list-component',
  standalone: true,
  imports: [TableModule, ButtonModule, RouterLink],
  templateUrl: './user-list-component.html',
  styleUrl: './user-list-component.css'
})
export class UserListComponent implements OnInit {

  constructor(private userService: UserService) {

  }

  users: UserInterface[] = [];

  deleteUser(id: number) {
    console.log(id)
  }

  ngOnInit(): void {
    this.userService.getUsers().subscribe(res => {
      this.users = res;
    })
  }


}
