import { Routes } from '@angular/router';
import { CategoriesFormComponent } from './components/categories-form-component/categories-form-component';
import { CategoryListComponent } from './components/category-list-component/category-list-component';
import { ZoneFormComponent } from './components/zone-form-component/zone-form-component';
import { ZoneListComponent } from './components/zone-list-component/zone-list-component';
import { UserListComponent } from './components/user-list-component/user-list-component';
import { UserFormComponent } from './components/user-form-component/user-form-component';

export const routes: Routes = [
    {
        path: 'category-form',
        component: CategoriesFormComponent
    },
    {
        path: 'category-form/:id',
        component: CategoriesFormComponent
    },
    {
        path: 'category-list',
        component: CategoryListComponent
    },
    {
        path: 'zone-form',
        component: ZoneFormComponent
    },
    {
        path: 'zone-list',
        component: ZoneListComponent
    },
    {
        path: 'zone-form/:id',
        component: ZoneFormComponent
    },
    {
        path: 'user-list',
        component: UserListComponent
    },
    {
        path: 'user-form/:id',
        component: UserFormComponent
    }, 
    {
        path: 'user-form',
        component: UserFormComponent
    }

];
