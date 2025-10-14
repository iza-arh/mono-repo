import { Component, computed, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav'
import { Header } from './components/header/header';
import { CustomSidenav } from './components/custom-sidenav/custom-sidenav';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MatSidenavModule, Header, CustomSidenav],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');

  collapsed = signal(false)

  width = computed(() => this.collapsed() ? 64 : 250)
}
