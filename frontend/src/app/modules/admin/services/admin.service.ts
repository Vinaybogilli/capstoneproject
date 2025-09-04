import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CustomerDTO {
  id: number;
  name: string;
  email: string;
  age?: number;
  accountType?: string;
  phoneNumber?: string;
  address?: string;
  aadharNumber?: string;
  panNumber?: string;
  kyc?: string;
}

export interface AccountDTO {
  id: number;
  name?: string;
  accountNumber: string;
  userName: string;
  balance: number;
  loan?: number;
  accountType: string;
  customerId?: number;
}

export interface TransactionDTO {
  fromAccount: string;
  toAccount: string;
  amount: number;
  status: string;
  external: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private baseUrl = 'http://localhost:8765/api/admin';

  constructor(private readonly http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt'); 
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getAllCustomers(page: number = 0, size: number = 10, search?: string): Observable<CustomerDTO[]> {
    let url = `${this.baseUrl}/getallcustomers?page=${page}&size=${size}`;
    if (search) url += `&search=${search}`;
    return this.http.get<CustomerDTO[]>(url, { headers: this.getAuthHeaders() });
  }

  getAllAccounts(page: number = 0, size: number = 10, search?: string): Observable<AccountDTO[]> {
    let url = `${this.baseUrl}/getallaccounts?page=${page}&size=${size}`;
    if (search) url += `&search=${search}`;
    return this.http.get<AccountDTO[]>(url, { headers: this.getAuthHeaders() });
  }

  getAllTransactions(page: number = 0, size: number = 10): Observable<TransactionDTO[]> {
    const url = `${this.baseUrl}/getalltransactions?page=${page}&size=${size}`;
    return this.http.get<TransactionDTO[]>(url, { headers: this.getAuthHeaders() });
  }
}
