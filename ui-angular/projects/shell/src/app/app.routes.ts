import { Routes } from '@angular/router';
import { loadRemoteModule } from '@angular-architects/native-federation';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/shell',
    pathMatch: 'full'
  },
  {
    path: 'shell',
    loadComponent: () => import('./features/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'products',
    loadChildren: () => loadRemoteModule('products', './routes').then(m => m.routes)
  }
];
