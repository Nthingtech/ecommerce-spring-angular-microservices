import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatGridListModule } from '@angular/material/grid-list';

@Component({
  selector: 'app-products-product-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatGridListModule],
  template: `
    <div style="padding: 20px;">
      <h1>Products Micro-Frontend</h1>
      <p>This is the products application loaded as a micro-frontend.</p>

      <mat-grid-list cols="3" rowHeight="300px" gutterSize="16px">
        <mat-grid-tile *ngFor="let product of mockProducts">
          <mat-card style="width: 100%; height: 100%;">
            <mat-card-header>
              <mat-card-title>{{ product.name }}</mat-card-title>
              <mat-card-subtitle>\${{ product.price }}</mat-card-subtitle>
            </mat-card-header>
            <mat-card-content>
              <p>{{ product.description }}</p>
            </mat-card-content>
            <mat-card-actions>
              <button mat-raised-button color="primary">Add to Cart</button>
            </mat-card-actions>
          </mat-card>
        </mat-grid-tile>
      </mat-grid-list>
    </div>
  `
})
export class ProductListComponent {
  mockProducts = [
    { id: 1, name: 'Laptop', price: 999.99, description: 'High-performance laptop' },
    { id: 2, name: 'Smartphone', price: 699.99, description: 'Latest smartphone' },
    { id: 3, name: 'Headphones', price: 199.99, description: 'Wireless headphones' },
    { id: 4, name: 'Tablet', price: 399.99, description: 'Portable tablet' },
    { id: 5, name: 'Camera', price: 799.99, description: 'Digital camera' },
    { id: 6, name: 'Watch', price: 299.99, description: 'Smart watch' }
  ];
}
