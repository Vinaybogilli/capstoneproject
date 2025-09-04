import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomerserviceService, Customer } from '../../../services/customerservice.service';
import { MaterialModule } from '../../../../material.module';
import { AuthService } from '../../../../auth/services/auth.service';
import { AccountService, Account } from '../../../services/account.service';
import { PaymentService, Payment } from '../../../services/payment.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MaterialModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  customerForm: FormGroup;
  accountForm: FormGroup;
  paymentForm: FormGroup;

  customerExists = false;
  accountExists = false;

  accountTypes = ['SAVINGS', 'CURRENT', 'FIXED'];
  kycStatuses = ['VERIFIED', 'PENDING', 'REJECTED'];
  activeSection: string = 'customer';

  balance: number = 0;
  transactions: Payment[] = [];

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerserviceService,
    private accountService: AccountService,
    private paymentService: PaymentService,
    private authService: AuthService,
    private snackbar: MatSnackBar
  ) {
    // customer form

  

    this.customerForm = this.fb.group({
      id: [null],
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      age: ['', [Validators.required, Validators.min(18)]],
      accountType: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^[6-9][0-9]{9}$/)]],
      address: ['', [Validators.required]],
      aadharNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{12}$/)]],
      panNumber: ['', [Validators.required]],
      kyc: ['VERIFIED']
    });

    // account form
    this.accountForm = this.fb.group({
      name: ['', Validators.required],
      userName: ['', Validators.required],
      pin: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(8)]],
      accountType: ['', Validators.required],
      customerId: [null]
    });

    // payment form
    this.paymentForm = this.fb.group({
      toAccountNumber: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(15)]],
      amount: ['', [Validators.required, Validators.min(1)]],
      transactionType: ['INTERNAL'] 
    });
  }

  ngOnInit(): void {
    const email = this.authService.email;
    if (email) {
      this.customerForm.get('email')?.setValue(email);
      this.fetchCustomerByEmail(email);
    }

    const accountId = localStorage.getItem('accountId');
    if (accountId) {
      this.accountExists = true;
      this.fetchBalance();
      this.fetchTransactions();
    }
  }

  
  fetchCustomerByEmail(email: string) {
    this.customerService.getCustomerByEmail(email).subscribe({
      next: (res: Customer | null) => {
        if (res) {
          this.customerForm.patchValue(res);
          this.customerExists = true;
          localStorage.setItem('customerId', res.id?.toString() || '');
        } else {
          this.customerExists = false;
          this.snackbar.open('No customer found. Please add your details.', 'Close', { duration: 3000 });
        }
      },
      error: (err: any) => {
        if (err.status === 404) {
          this.customerExists = false;
          this.snackbar.open('Customer not found. Add your details.', 'Close', { duration: 3000 });
        } else {
          console.error(err);
          this.snackbar.open('Failed to fetch customer details', 'Close', { duration: 3000 });
        }
      }
    });
  }

  saveCustomer() {
    if (this.customerForm.invalid) {
      this.snackbar.open('Please fill all fields correctly', 'Close', { duration: 3000 });
      return;
    }

    const customerData = this.customerForm.value;

    if (this.customerExists) {
      const customerId = localStorage.getItem('customerId');
      if (!customerId) return;

      this.customerService.updateCustomer(customerId, customerData).subscribe({
        next: () => this.snackbar.open('Customer updated successfully!', 'Close', { duration: 3000 }),
        error: (err: any) => {
          console.error(err);
          this.snackbar.open('Failed to update customer', 'Close', { duration: 3000 });
        }
      });
    } else {
      this.customerService.addCustomer(customerData).subscribe({
        next: (res: Customer) => {
          this.snackbar.open('Customer added successfully!', 'Close', { duration: 3000 });
          this.customerExists = true;
          localStorage.setItem('customerId', res.id?.toString() || '');
        },
        error: (err: any) => {
          console.error(err);
          this.snackbar.open('Failed to add customer', 'Close', { duration: 3000 });
        }
      });
    }
  }

  
createAccount() {
  if (!this.customerExists) {
    this.snackbar.open('Please add customer details before creating an account!', 'Close', { duration: 3000 });
    return;
  }

  if (this.accountForm.invalid) {
    this.snackbar.open('Fill all account fields properly', 'Close', { duration: 3000 });
    return;
  }

  let customerId = this.customerForm.get('id')?.value;

  // If customerId is not present in the form, try fetching it by email
  if (!customerId) {
    const email = this.customerForm.get('email')?.value;
    if (!email) {
      this.snackbar.open('Email not provided. Please enter email.', 'Close', { duration: 3000 });
      return;
    }

    this.fetchCustomerByEmailAndCreateAccount(email);
    return;
  }

  this.proceedAccountCreation(customerId);
}

private fetchCustomerByEmailAndCreateAccount(email: string) {
  this.customerService.getCustomerByEmail(email).subscribe({
    next: (res: Customer | null) => {
      if (res && res.id) {
        this.customerForm.patchValue(res);
        this.customerExists = true;
        localStorage.setItem('customerId', res.id.toString());
        this.proceedAccountCreation(res.id);
      } else {
        this.snackbar.open('No customer found. Please add your details.', 'Close', { duration: 3000 });
      }
    },
    error: (err: any) => {
      console.error(err);
      this.snackbar.open('Failed to fetch customer details', 'Close', { duration: 3000 });
    }
  });
}

private proceedAccountCreation(customerId: number) {
  const accountData = {
    ...this.accountForm.value,
    customerId: parseInt(customerId.toString(), 10),
  };

  this.accountService.createAccount(accountData).subscribe({
    next: (res: Account) => {
      this.snackbar.open('Account created successfully!', 'Close', { duration: 3000 });
      this.accountExists = true;
      localStorage.setItem('accountId', res.id?.toString() || '');
      this.fetchBalance();
      this.fetchTransactions();
    },
    error: (err: any) => {
      console.error(err);
      this.snackbar.open('Failed to create account', 'Close', { duration: 3000 });
    },
  });
}



  // ---------------- Payment Section ----------------
makePayment() {
  if (this.paymentForm.invalid) {
    this.snackbar.open('Please fill all payment details correctly', 'Close', { duration: 3000 });
    return;
  }

  const customerId = this.customerForm.get('id')?.value;
  if (!customerId) {
    this.snackbar.open('No customer found. Please add customer details first.', 'Close', { duration: 3000 });
    return;
  }

  // Get the latest account from localStorage
  const storedAccount = localStorage.getItem('account');
  if (!storedAccount) {
    this.snackbar.open('No account found. Please select an account first.', 'Close', { duration: 3000 });
    return;
  }

  const account = JSON.parse(storedAccount);

  // Prepare payment data using the latest account number
  const paymentData = {
    ...this.paymentForm.value,
    fromAccountNumber: account.accountNumber
  };

  // Call backend and expect a string response
  this.paymentService.makePayment(paymentData).subscribe({
  next: (message: string) => {
    this.snackbar.open(message, 'Close', { duration: 3000 });
    this.fetchBalance();
    this.fetchTransactions();
    this.paymentForm.reset({ transactionType: 'INTERNAL' });
  },
  error: (err: any) => {
    console.error(err);
    this.snackbar.open('Payment failed. Please try again.', 'Close', { duration: 3000 });
  }
});
}




fetchBalance() {
  const email = this.authService.email;
  this.customerService.getCustomerByEmail(email).subscribe({
    next: (customer) => {
      if (!customer?.id) {
        this.snackbar.open('Customer not found', 'Close', { duration: 3000 });
        return;
      }

      console.log('Customer found:', customer.name);
      const customerId = customer.id;

      // Step 2: fetch account using customerId
      this.accountService.getAccountByCustomerId(customerId).subscribe({
        next: (account) => {
          if (!account?.id) {
            this.snackbar.open('Account not found for this customer', 'Close', { duration: 3000 });
            return;
          }

          const accountId = account.id;
          localStorage.setItem('account', JSON.stringify(account));
          console.log('Account found:', account.accountNumber);

          // Step 3: fetch balance using accountId
          this.accountService.getBalance(accountId).subscribe({
            next: (balance) => {
              console.log('Fetched balance:', balance);
              this.balance = balance;
            },
            error: (err) => {
              console.error('Failed to fetch balance', err);
              this.balance = 0;
            }
          });
        },
        error: (err) => {
          console.error('Failed to fetch account', err);
        }
      });
    },
    error: (err) => {
      console.error('Failed to fetch customer', err);
    }
  });
}


fetchTransactions() {
  const customerId = this.customerForm.get('id')?.value;
  if (!customerId) return;

  
}
}
