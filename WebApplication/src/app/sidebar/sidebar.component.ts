import { Component, OnInit } from '@angular/core';

export interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
}

export const ROUTES: RouteInfo[] = [
  // { path: '/dashboard', title: 'Dashboard', icon: 'ti-server', class: '' },
  { path: '/file', title: 'Estudiantes', icon: 'ti-user', class: '' },
  // { path: '/users', title: 'Usuarios', icon: 'ti-user', class: '' }
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
