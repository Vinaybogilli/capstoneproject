import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Account {
  id?: number;
  Name: string;
  accountNumber?: string;
  userName: string;
  pin: string;
  balance?: number;
  loan?: number;
  accountType: string;
  customerId: number;
}

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  

  private baseUrl = 'http://localhost:8765/api/bank';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token'); // Get token from localStorage
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token || ''}` // Add token here
    });
  }

  createAccount(account: Account): Observable<Account> {
    return this.http.post<Account>(
      `${this.baseUrl}/createaccount`,
      account,
      { headers: this.getAuthHeaders() }
    );
  }

  getAccountByCustomer(customerId: number): Observable<Account> {
    return this.http.get<Account>(
      `${this.baseUrl}/getbycustomer/${customerId}`,
      { headers: this.getAuthHeaders() }
    );
  }

  getBalance(accountId: number): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/checkbalance/${accountId}`,
      { headers: this.getAuthHeaders() }
    );
  }

  getAccountByCustomerId(customerId: number): Observable<Account> {
  return this.http.get<Account>(
    `${this.baseUrl}/getaccountbycustomerid/${customerId}`,
    { headers: this.getAuthHeaders() }
  );
}

}


