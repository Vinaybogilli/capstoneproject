import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MaterialModule } from '../../../material.module';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule, MaterialModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm!: FormGroup;
  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private snackbar: MatSnackBar,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]]
    });
  }

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.snackbar.open('Please fill all fields', 'Close', { duration: 3000 });
      return;
    }

    this.authService.login(this.loginForm.value).subscribe({
      next: (res) => {
        console.log('Login Response:', res); 

        if (res.userId != null && res.userRole != null && res.jwt != null) {
          
          localStorage.setItem('jwt', res.jwt);
          localStorage.setItem('userRole', res.userRole);
          this.authService.email = this.loginForm.value.email;


          
          if (res.userRole === 'ADMIN') {
            this.router.navigateByUrl('/admin/dashboard');
          } else if (res.userRole === 'CUSTOMER') {
            this.router.navigateByUrl('/user/dashboard');
          }
          

          this.snackbar.open('Login Successful', 'Close', { duration: 3000 });
        } else {
          this.snackbar.open('Login failed. Invalid credentials', 'Close', { duration: 5000 });
        }
      },
      error: (err) => {
        console.error('Login error:', err);
        this.snackbar.open('An error occurred. Try again.', 'Close', { duration: 5000 });
      }
    });
  }
}
