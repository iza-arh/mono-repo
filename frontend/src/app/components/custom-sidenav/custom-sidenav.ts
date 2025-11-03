import { Component, input, signal, computed, OnInit } from '@angular/core';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '@auth0/auth0-angular';

interface MenuItem {
  path: string;
  icon: string;
  label: string;
  roles: string[];
}

@Component({
  selector: 'app-custom-sidenav',
  standalone: true,
  imports: [MatListModule, MatIconModule, CommonModule, RouterLink],
  templateUrl: './custom-sidenav.html',
  styleUrls: ['./custom-sidenav.css']
})

export class CustomSidenav implements OnInit {
  collapsed = input.required<boolean>();

  private allItems = signal<MenuItem[]>([
    { path: '/home', icon: 'home', label: 'Home', roles: ['Administrador', 'Técnico', 'Encargado municipal', 'Ciudadano'] },
    { path: '/category-form', icon: 'description', label: 'add category', roles: ['Administrador'] },
    { path: '/category-list', icon: 'view_list', label: 'show categories', roles: ['Administrador'] },
    { path: '/zone-form', icon: 'description', label: 'add zone', roles: ['Administrador'] },
    { path: '/zone-list', icon: 'view_list', label: 'show zones', roles: ['Administrador'] },
    { path: '/user-list', icon: 'view_list', label: 'show users', roles: ['Administrador'] },
    { path: '/user-form', icon: 'person_add', label: 'add user', roles: ['Administrador'] },
    { path: '/report-form', icon: 'report_problem', label: 'add report', roles: ['Administrador', 'Técnico', 'Encargado municipal', 'Ciudadano'] },
    { path: '/report-list', icon: 'list_alt', label: 'show reports', roles: ['Administrador', 'Técnico', 'Encargado municipal', 'Ciudadano'] },

  ]);

  constructor(private auth: AuthService) {}

  private user = signal<any | null>(null);

  //Get the roles of the custom claim
  private roles = computed(() => this.user()?.['https://urbano-fix.com/roles'] || []);

  //Filter menu options based on roles
  items = computed(() =>
    this.allItems().filter(item =>
      item.roles.some(role => this.roles().includes(role))
    )
  );

  //When the component is initialized, we obtain the authenticated user
  ngOnInit(): void {
    this.auth.user$.subscribe(user => {
      this.user.set(user);
    });
  }
}
