import { Component, OnInit } from '@angular/core';
import { UsersService } from '../services/users.service';
import { ToastNotificationService, ToastType } from 'src/app/services/toast-notification.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  users: any;
  isVisible = false;
  modalTitle = 'Crear Usuario';
  userForm: FormGroup;

  constructor(private userService: UsersService,
    private toastNotificationService: ToastNotificationService,
    private fb: FormBuilder) { }

  ngOnInit() {
  }

  getFiles() {
    this.userService.getUsers().then(files => {
      this.users = files;
    }).catch(() => {
      this.displayToast('Error', 'No se lograron cargar los archivos', ToastType.Error);
    });
  }

  displayToast(title: string, message: string, toastType: ToastType): void {
    this.toastNotificationService.show({
      title: title,
      message: message
    }, toastType);
  }
  handleCancel(): void {
    this.isVisible = false;
  }
  handleOk(): void {
    const fileForm = this.userForm.getRawValue();
    // this.createFile(fileForm);
    this.isVisible = false;
    this.userForm.reset();

  }

  showModal() {
    this.isVisible = true;
  }
}
