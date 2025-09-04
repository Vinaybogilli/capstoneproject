import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PaymentRequest {
  toAccountNumber: string;
  amount: number;
  transactionType: 'INTERNAL' | 'EXTERNAL';
  fromAccountNumber: string;
}

export interface Payment {
  id?: number;
  fromAccountNumber: string;
  toAccountNumber: string;
  amount: number;
  transactionType: 'INTERNAL' | 'EXTERNAL';
  timestamp?: Date;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private baseUrl = 'http://localhost:8765/payments';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt'); 
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token || ''}`
    });
  }

 makePayment(request: PaymentRequest): Observable<string> {
  return this.http.post(
    `${this.baseUrl}/internal`,
    request,
    {
      headers: this.getAuthHeaders(),
      responseType: 'text' 
    }
  );
}

  getTransactions(accountId: string): Observable<Payment[]> {
    return this.http.get<Payment[]>(
      `${this.baseUrl}/transactions/${accountId}`,
      { headers: this.getAuthHeaders() }
    );
  }

  getBalance(accountId: number): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/balance/${accountId}`,
      { headers: this.getAuthHeaders() }
    );
  }
}
