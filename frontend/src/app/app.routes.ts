import { Routes } from '@angular/router';
import { CategoriesFormComponent } from './components/categories-form-component/categories-form-component';
import { CategoryListComponent } from './components/category-list-component/category-list-component';
import { ZoneFormComponent } from './components/zone-form-component/zone-form-component';
import { ZoneListComponent } from './components/zone-list-component/zone-list-component';
import { UserListComponent } from './components/user-list-component/user-list-component';
import { UserFormComponent } from './components/user-form-component/user-form-component';
import { ReportsFormComponent } from './components/reports-form-component/reports-form-component';
import { ReportListComponent } from './components/report-list-component/report-list-component';
import { LoginComponent } from './auth/login/login.component/login.component';
import { AuthGuard } from '@auth0/auth0-angular';
import { MainLayout } from './layouts/main-layout/main-layout';
import { HomeComponent } from './components/home/home';
import { RoleGuard } from './role.guards';

export const routes: Routes = [
    //Default redirection
    {path: '', redirectTo: 'login', pathMatch: 'full'},

    //Public route (no layout)
    {
        path: 'login',
        component: LoginComponent
    },

    //Protected routes with layout
    {
        path: '',
        component: MainLayout,
        canActivate: [AuthGuard],
        children:[
            //Only 'Administrador' can access these routes. 
            { path: 'category-form', component:CategoriesFormComponent, canActivate: [RoleGuard], data: { roles: ['Administrador'] } },
            { path: 'category-form/:id', component: CategoriesFormComponent, canActivate: [RoleGuard], data: { roles: ['Administrador'] } },
            { path: 'category-list', component: CategoryListComponent, canActivate: [RoleGuard], data: { roles: ['Administrador'] } },
            { path: 'zone-form', component: ZoneFormComponent, canActivate: [RoleGuard], data: { roles: ['Administrador'] } },
            { path: 'zone-form/:id', component: ZoneFormComponent, canActivate: [RoleGuard], data: { roles: ['Administrador'] } },
            { path: 'zone-list', component: ZoneListComponent, canActivate: [RoleGuard], data: { roles: ['Administrador'] } },
            { path: 'user-list', component: UserListComponent, canActivate: [RoleGuard], data: { roles: ['Administrador'] }},
            { path: 'user-form/:id', component: UserFormComponent, canActivate: [RoleGuard], data: { roles: ['Administrador'] }}, 
            { path: 'user-form', component: UserFormComponent, canActivate: [RoleGuard], data: { roles: ['Administrador'] }},
            //Only 'Técnico' role can access these routes.

            //Only 'Encargado municipal' role can access these routes.

            //Only 'Ciudadano' role can access these routes.

            //All roles have access to these routes.
            { path: 'home', component: HomeComponent, canActivate: [RoleGuard], data: { roles: ['Administrador', 'Técnico', 'Encargado municipal', 'Ciudadano'] } },
            { path: 'report-form', component: ReportsFormComponent, canActivate: [RoleGuard], data: { roles: ['Administrador', 'Técnico', 'Encargado municipal', 'Ciudadano'] }},
            { path: 'report-list', component: ReportListComponent, canActivate: [RoleGuard], data: { roles: ['Administrador', 'Técnico', 'Encargado municipal', 'Ciudadano'] }},
        ],
    },


];
