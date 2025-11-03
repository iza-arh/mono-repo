import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Header } from '../../components/header/header';
import { CustomSidenav } from '../../components/custom-sidenav/custom-sidenav';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    MatSidenavModule,
    Header,
    CustomSidenav
  ],
  templateUrl: './main-layout.html',
})
export class MainLayout {
  // Control del colapso del menú lateral
  collapsed = signal(false);

  // Ancho dinámico según el estado del colapso
  width() {
    return this.collapsed() ? 80 : 250;
  }
}