import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  registerForm: FormGroup;

  constructor(private builder: FormBuilder) { }

  ngOnInit() {
    this.registerForm = this.builder.group({
      email: [null, [Validators.email]],
      password: [null, [Validators.required]],
      passwordConfirmation: [null, [Validators.required]],
      name: [null, [Validators.required]],
      lastName: [null, [Validators.required]],
      identificationType: [null, [Validators.required]],
      identification: [null, [Validators.required]],
      birthdate: [null, [Validators.required]],
    });
  }

  register() {

  }

}
