import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../material.module';

@Component({
  selector: 'app-home-component',
  standalone: true,
  imports: [RouterLink, MaterialModule, CommonModule],
  templateUrl: './home-component.component.html',
  styleUrls: ['./home-component.component.css']
})
export class HomeComponentComponent {

  constructor(private router: Router) {}

  goToLogin(): void {
    this.router.navigateByUrl('/login');
  }

  goToSignup(): void {
    this.router.navigateByUrl('/signup');
  }
}
