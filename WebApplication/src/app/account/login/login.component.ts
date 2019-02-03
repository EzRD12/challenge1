import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.service';
import { ToastType, ToastNotificationService } from 'src/app/services/toast-notification.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
    private accountService: AccountService,
    private toastNotificationService: ToastNotificationService,
    private router: Router) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  authenticate() {
    const user = this.loginForm.getRawValue();

    // tslint:disable-next-line:no-shadowed-variable
    this.accountService.login(user).then((user) => {
      localStorage.setItem('challengeToken', JSON.stringify(user));
      this.router.navigate(['../../file']);
    }).catch(() => {
      this.displayToast('Error', 'Usuario invalido', ToastType.Error);
    });
  }

  displayToast(title: string, message: string, toastType: ToastType): void {
    this.toastNotificationService.show({
      title: title,
      message: message
    }, toastType);
  }

}
