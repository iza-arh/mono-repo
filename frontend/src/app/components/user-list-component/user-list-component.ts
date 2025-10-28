import { Component } from '@angular/core';
import { UserInterface } from '../../models/interface/user.interface';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-user-list-component',
  standalone: true,
  imports: [TableModule, ButtonModule, RouterLink],
  templateUrl: './user-list-component.html',
  styleUrl: './user-list-component.css'
})
export class UserListComponent {

  users: UserInterface[] = [{
    id: 1,
    email: 'john.doe@example.com',
    name: 'John',
    lastName: 'Doe',
    role: 'Admin',
    phone: '+1-555-123-4567'
  },
  {
    id: 2,
    email: 'jane.smith@example.com',
    name: 'Jane',
    lastName: 'Smith',
    role: 'Editor',
    phone: '+1-555-987-6543'
  },
  {
    id: 3,
    email: 'michael.brown@example.com',
    name: 'Michael',
    lastName: 'Brown',
    role: 'Viewer',
    phone: '+1-555-222-3333'
  }
  ];

  deleteUser(id: number) {
    console.log(id)
  }



}
