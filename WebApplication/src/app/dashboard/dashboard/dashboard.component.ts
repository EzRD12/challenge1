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

  constructor() { }

  ngOnInit() {
    this.wordFiles = 1;
    this.excelFiles = 2;
    this.powerPointFiles = 3;
  }

}
