import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ToastNotificationService, ToastType } from 'src/app/services/toast-notification.service';
import { AccountService } from 'src/app/services/account.service';
import { Router } from '@angular/router';
import { UsersService } from 'src/app/services/users.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  registerForm: FormGroup;

  constructor(private builder: FormBuilder,
    private accountService: AccountService,
    private router: Router,
    private toastNotificationService: ToastNotificationService,
    private userService: UsersService) { }

  ngOnInit() {
    this.registerForm = this.builder.group({
      username: [null, [Validators.email]],
      password: [null, [Validators.required]],
      passwordConfirmation: [null, [Validators.required]],
      name: [null, [Validators.required]]
    });
  }

  register() {
    const user = this.registerForm.getRawValue();

    if (user.password !== user.passwordConfirmation) {
      this.displayToast('Inconsistencia en contraseñas', 'Las contraseñas insertadas no coinciden', ToastType.Error);
      return;
    }

    this.accountService.signUp(user).then(() => {
      this.displayToast('Registro completo', 'La operacion ha sido exitosa', ToastType.Success);
      this.router.navigate(['../../login']);
    }).catch(() => {
      this.displayToast('Error', 'No se logró registrar correctamente', ToastType.Error);
    });
  }

  displayToast(title: string, message: string, toastType: ToastType): void {
    this.toastNotificationService.show({
      title: title,
      message: message
    }, toastType);
  }

  createUser(user) {
    this.userService.createUser(user).then(() => {
      this.displayToast('Usuario creado', '', ToastType.Success);
    }).catch(() => {
      this.displayToast('Error', 'No se logro crear el usuario', ToastType.Error);
    });
  }

}
