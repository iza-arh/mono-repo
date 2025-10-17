import { Routes } from '@angular/router';
import { CategoriesFormComponent } from './components/categories-form-component/categories-form-component';
import { CategoryListComponent } from './components/category-list-component/category-list-component';
import { ZoneFormComponent } from './components/zone-form-component/zone-form-component';
import { ZoneListComponent } from './components/zone-list-component/zone-list-component';

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
    }
];
