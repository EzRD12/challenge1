import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserProfile } from '../models/user-profile';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient) { }

  signUp(user) {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(user);
      }, 300);
    });
  }

  login(user) {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(user);
      }, 300);
    });
  }

  logout(): void {
    localStorage.clear();
  }

  public get currentUser(): UserProfile {
    return JSON.parse(localStorage.getItem('challengeToken')) as UserProfile;
  }
}
