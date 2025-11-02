import { Component, OnInit, output } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { AuthService } from '@auth0/auth0-angular';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, MatToolbarModule, MatIconModule, MatButtonModule, ButtonModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
})
export class Header implements OnInit {
  onToggle = output<void>();

  user: any;
  role: string = '';

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
    this.auth.user$.subscribe((profile) => {
      this.user = profile;
      // Leer el rol desde el claim personalizado en el token
      const roles = (profile as any)?.['https://urbano-fix.com/roles'] || [];
      this.role = roles.length > 0 ? roles[0] : 'Sin rol';
    });
  }

  logout(): void {
    this.auth.logout({ logoutParams: { returnTo: window.location.origin } });
  }
}
