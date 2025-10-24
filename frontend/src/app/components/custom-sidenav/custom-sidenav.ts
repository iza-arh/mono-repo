import { Component, input, signal, Signal } from '@angular/core';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

interface MenuItem {
  path: string;
  icon: string;
  label: string;
}

@Component({
  selector: 'app-custom-sidenav',
  standalone: true,
  imports: [MatListModule, MatIconModule, CommonModule, RouterLink],
  templateUrl: './custom-sidenav.html',
  styleUrls: ['./custom-sidenav.css']
})
export class CustomSidenav {
  items = signal<MenuItem[]>([
    { path: '/category-form', icon: 'description', label: 'add category' },
    { path: '/category-list', icon: 'view_list', label: 'show categories' },
    { path: '/zone-form', icon: 'description', label: 'add zone' },
    { path: '/zone-list', icon: 'view_list', label: 'show zones' },
    { path: '/user-list', icon: 'view_list', label: 'show users' }
  ]);

  collapsed = input.required<boolean>()
}

