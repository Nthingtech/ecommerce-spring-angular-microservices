import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-shell-home',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule],
  template: `
    <div style="padding: 20px;">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Welcome to the E-commerce Shell</mat-card-title>
          <mat-card-subtitle>Micro-frontend Architecture</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <p>This is the shell application that hosts other micro-frontends.</p>
          <p>Navigate to different sections using the links below:</p>
        </mat-card-content>
        <mat-card-actions>
          <button mat-raised-button color="primary" routerLink="/products">
            View Products
          </button>
        </mat-card-actions>
      </mat-card>
    </div>
  `
})
export class HomeComponent {
}
