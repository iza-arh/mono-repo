import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '@auth0/auth0-angular';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private auth: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot) {
    const expectedRoles = route.data['roles'] as string[];

    return this.auth.user$.pipe(
      map(user => {
        // Here you get the roles from the custom claim
        const roles = user?.['https://urbano-fix.com/roles'] || [];

        const hasRole = expectedRoles.some(role => roles.includes(role));

        if (!hasRole) {
          // If you don't have permission, redirect to unauthorized page
          this.router.navigate(['/unauthorized']);
          return false;
        }

        return true;
      })
    );
  }
}
