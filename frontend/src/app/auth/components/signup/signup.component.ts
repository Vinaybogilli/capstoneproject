import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MaterialModule } from '../../../material.module';
import { FormGroup, Validators, FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from './../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [RouterModule, MaterialModule, FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  signupForm!: FormGroup;
  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private snackbar: MatSnackBar,
    private router: Router
  ) {
    this.signupForm = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(5), Validators.maxLength(50), Validators.pattern(/^[a-zA-Z\s]+$/)]],
      email: [
        null,
        [
          Validators.required,
          Validators.email,
          Validators.pattern(/^(?=[a-zA-Z0-9._%+-]{1,64}@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)
        ]
      ],
      password: [
        null,
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(20),
          Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=*!?])[A-Za-z\d@#$%^&+=*!?]{8,20}$/)
        ]
      ]
    });
  }

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

  // Submit the form
  onSubmit() {
    if (this.signupForm.invalid) {
      this.snackbar.open('Please fill all fields correctly.', 'Close', { duration: 5000 });
      return;
    }

    // Send only the fields backend expects
    const signupData = {
      name: this.signupForm.value.name,
      email: this.signupForm.value.email,
      password: this.signupForm.value.password,
      userRole: 'USER'
    };

    console.log('Signup Data:', signupData);

    this.authService.signup(signupData).subscribe({
      next: (res) => {
        if (res.id != null) {
          this.snackbar.open('Signup successful! Redirecting to login...', 'Close', { duration: 3000 });
          this.router.navigateByUrl('/login');
        } else {
          this.snackbar.open('Signup failed. Try again.', 'Close', { duration: 5000 });
        }
      },
      error: (err) => {
        console.error('Signup error:', err);
        this.snackbar.open('An error occurred. Try again.', 'Close', { duration: 5000 });
      }
    });
  }
}
