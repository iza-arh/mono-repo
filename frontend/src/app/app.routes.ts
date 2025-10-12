import { Routes } from '@angular/router';
import { CategoriesFormComponent } from './components/categories-form-component/categories-form-component';
import { CategoryListComponent } from './components/category-list-component/category-list-component';

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
    }
];
