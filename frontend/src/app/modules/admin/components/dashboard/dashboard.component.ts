import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService, CustomerDTO, AccountDTO, TransactionDTO } from '../../services/admin.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatToolbarModule,
    MatButtonModule,
    MatTabsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  dummyCustomers: CustomerDTO[] = [];
  dummyAccounts: AccountDTO[] = [];
  dummyTransactions: TransactionDTO[] = [];

  customerPage = 0;
  customerSize = 10;
  accountPage = 0;
  accountSize = 10;
  transactionPage = 0;
  transactionSize = 10;

  customerSearch: string = '';
  accountSearch: string = '';

  constructor(private readonly adminService: AdminService) {}

  ngOnInit(): void {
    this.loadCustomers();
    this.loadAccounts();
    this.loadTransactions();
  }

  loadCustomers(): void {
    this.adminService.getAllCustomers(this.customerPage, this.customerSize, this.customerSearch)
      .subscribe((res: any) => this.dummyCustomers = res.content || []);
  }

  searchCustomers(): void {
    this.customerPage = 0;
    this.loadCustomers();
  }

  changeCustomerPage(event: any): void {
    this.customerPage = event.pageIndex;
    this.customerSize = event.pageSize;
    this.loadCustomers();
  }

  loadAccounts(): void {
    this.adminService.getAllAccounts(this.accountPage, this.accountSize, this.accountSearch)
      .subscribe((res: any) => this.dummyAccounts = res.content || []);
  }

  searchAccounts(): void {
    this.accountPage = 0;
    this.loadAccounts();
  }

  changeAccountPage(event: any): void {
    this.accountPage = event.pageIndex;
    this.accountSize = event.pageSize;
    this.loadAccounts();
  }

  loadTransactions(): void {
  this.adminService.getAllTransactions(this.transactionPage, this.transactionSize)
    .subscribe((res: TransactionDTO[]) => this.dummyTransactions = res || []);
}

  changeTransactionPage(event: any): void {
    this.transactionPage = event.pageIndex;
    this.transactionSize = event.pageSize;
    this.loadTransactions();
  }
}
