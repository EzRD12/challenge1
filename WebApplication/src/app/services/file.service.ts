import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private http: HttpClient) { }

  createFile(fileForm) {
    if (fileForm.FileType == 0) {
      return this.http.post(`${environment.apiBaseUrl}/excel`, fileForm).toPromise();
    } else if (fileForm.FileType == 1) {
      return this.http.post(`${environment.apiBaseUrl}/word`, fileForm).toPromise();
    } else {
      return this.http.post(`${environment.apiBaseUrl}/power-point`, fileForm).toPromise();
    }
  }

  deleteFile(file) {
      return this.http.post(`${environment.apiBaseUrl}/files/delete`, file).toPromise();
  }

  getFiles() {
    return this.http.get<any[]>(`${environment.apiBaseUrl}/files`).toPromise();
  }

  updateFile(fileForm) {
    if (fileForm.FileType == 0) {
      return this.http.put(`${environment.apiBaseUrl}/excel`, fileForm).toPromise();
    } else if (fileForm.FileType == 1) {
      return this.http.put(`${environment.apiBaseUrl}/word`, fileForm).toPromise();
    } else {
      return this.http.put(`${environment.apiBaseUrl}/power-point`, fileForm).toPromise();
    }
  }
}
