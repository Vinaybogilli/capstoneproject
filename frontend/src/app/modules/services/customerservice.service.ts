import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Customer {
  id?: number;
  name: string;
  email: string;
  age: number;
  phoneNumber: string;
  address: string;
  aadharNumber: string;
  panNumber: string;
  kyc: string;
}

@Injectable({
  providedIn: 'root'
})
export class CustomerserviceService {

  private baseUrl = 'http://localhost:8765/api/customer';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt'); 
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  getCustomerByEmail(email: string): Observable<Customer | null> {
  return this.http.get<Customer | null>(
    `${this.baseUrl}/getcustomerbyemail/${encodeURIComponent(email)}`, 
    { headers: this.getHeaders() }
  );
}


  addCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(
      `${this.baseUrl}/createcustomer`, 
      customer, 
      { headers: this.getHeaders() }
    );
  }

  updateCustomer(id: string, customer: Customer): Observable<Customer> {
    return this.http.patch<Customer>(
      `${this.baseUrl}/updatecustomer/${id}`, 
      customer, 
      { headers: this.getHeaders() }
    );
  }

  
}
