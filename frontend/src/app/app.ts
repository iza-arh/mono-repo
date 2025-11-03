import { Component, computed, signal, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav'
import { AuthService } from '@auth0/auth0-angular';
import { Router } from '@angular/router'

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MatSidenavModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('frontend');
  constructor(private auth: AuthService, private router: Router){}

  ngOnInit(){
    this.auth.appState$.subscribe((state)=>{
      if(state?.target){
        this.router.navigate([state.target]);
      }
    });
  }

  collapsed = signal(false)

  width = computed(() => this.collapsed() ? 64 : 250)
}
