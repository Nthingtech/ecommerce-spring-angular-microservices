import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/product-list/product-list.component').then(m => m.ProductListComponent)
  }
];
