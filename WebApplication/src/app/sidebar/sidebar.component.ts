import { Component, OnInit } from '@angular/core';

export interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
}

export const ROUTES: RouteInfo[] = [
  { path: '/home', title: 'Inicio', icon: 'ti-home', class: '' },
  { path: '/profile', title: 'Perfil', icon: 'ti-user', class: '' },
  { path: '/staff', title: 'Personal', icon: 'ti-view-list-alt', class: '' }
];

@Component({
  selector: 'app-sidebar-cmp',
  templateUrl: 'sidebar.component.html',
})

export class SidebarComponent implements OnInit {
  public menuItems: any[];

  constructor() {
  }

  ngOnInit() {
    this.menuItems = ROUTES;
  }
}
