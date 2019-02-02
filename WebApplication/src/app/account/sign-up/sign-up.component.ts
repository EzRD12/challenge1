import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ToastNotificationService, ToastType } from 'src/app/services/toast-notification.service';
import { AccountService } from 'src/app/services/account.service';
import { Router } from '@angular/router';

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
    private toastNotificationService: ToastNotificationService) { }

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

    this.accountService.signUp(user).then((user) => {
      localStorage.setItem('challengeToken', JSON.stringify(user));
      this.displayToast('Registro completo', 'La operacion ha sido exitosa', ToastType.Success);
      this.router.navigate(['../../dashboard']);
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

}
