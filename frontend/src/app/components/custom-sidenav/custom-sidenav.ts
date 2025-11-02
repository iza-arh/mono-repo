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
    { path: '/category-form', icon: 'description', label: 'add category', roles: ['Administrador', 'Técnico', 'Encargado municipal', 'Ciudadano'] },
    { path: '/category-list', icon: 'view_list', label: 'show categories', roles: ['Administrador', 'Técnico', 'Encargado municipal', 'Ciudadano'] },
    { path: '/zone-form', icon: 'description', label: 'add zone', roles: ['Administrador', 'Técnico', 'Encargado municipal', 'Ciudadano'] },
    { path: '/zone-list', icon: 'view_list', label: 'show zones', roles: ['Administrador', 'Técnico', 'Encargado municipal'] },
  ]);

  constructor(private auth: AuthService) {}

  private user = signal<any | null>(null);

  // 👇 obtiene los roles del claim personalizado
  private roles = computed(() => this.user()?.['https://urbano-fix.com/roles'] || []);

  // 👇 filtra las opciones del menú según los roles
  items = computed(() =>
    this.allItems().filter(item =>
      item.roles.some(role => this.roles().includes(role))
    )
  );

  // 🚀 Al inicializar el componente, obtenemos el usuario autenticado
  ngOnInit(): void {
    this.auth.user$.subscribe(user => {
      this.user.set(user);
      console.log('Usuario logueado:', user);
      console.log('Roles detectados:', user?.['https://urbano-fix.com/roles']);
    });
  }
}
