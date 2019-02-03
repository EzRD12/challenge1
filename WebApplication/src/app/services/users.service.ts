import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

constructor(private http: HttpClient) { }

createUser(user) {
  return this.http.post<any[]>(`${environment.apiBaseUrl}/user`, user).toPromise();
}

getUsers() {
  return this.http.get<any[]>(`${environment.apiBaseUrl}/user`).toPromise();
}
}
