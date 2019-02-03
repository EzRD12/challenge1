import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private http: HttpClient) { }

  createFile(students) {
    return this.http.post(`${environment.apiBaseUrl}/createStudent`, students).toPromise();
  }

  getFiles() {
    return this.http.get<any[]>(`${environment.apiBaseUrl}/files`).toPromise();
  }
}
