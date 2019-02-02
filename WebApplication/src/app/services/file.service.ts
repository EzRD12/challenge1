import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private http: HttpClient) { }

  createFile(fileForm) {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(fileForm);
      }, 300);
    });
  }

  getFiles() {
    return this.http.get<any[]>(`${environment.apiBaseUrl}/files`).toPromise();
  }
}
