import { FileType } from './../../enums/file-type';
import { FileService } from './../../services/file.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  wordFiles: number;
  excelFiles: number;
  powerPointFiles: number;

  constructor(private fileService: FileService) { }

  ngOnInit() {
    this.fileService.getFiles().then(files => {
      this.wordFiles = files.filter(file => file.Type === 'Word').length;
      this.excelFiles = files.filter(file => file.Type === 'Excel').length;
      this.powerPointFiles = files.filter(file => file.Type === 'PowerPoint').length;
    });
  }

}
