import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserProfile } from '../models/user-profile';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient) { }

  signUp(user) {
    return this.http.post(`${environment.apiBaseUrl}/user`, user).toPromise();
  }

  login(request) {
    return this.http.post<any>(`${environment.apiBaseUrl}/user/authenticate`, request).toPromise();
  }

  logout(): void {
    localStorage.clear();
  }

  public get currentUser() {
    return JSON.parse(localStorage.getItem('challengeToken'));
  }
}
