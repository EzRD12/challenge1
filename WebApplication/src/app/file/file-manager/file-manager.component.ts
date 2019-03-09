import { AccountService } from 'src/app/services/account.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ToastNotificationService, ToastType } from 'src/app/services/toast-notification.service';
import { FileService } from 'src/app/services/file.service';

@Component({
  selector: 'app-file-manager',
  templateUrl: './file-manager.component.html',
  styleUrls: ['./file-manager.component.css']
})
export class FileManagerComponent implements OnInit {
  files: any[] = [];
  isVisible = false;
  fileForm: FormGroup;
  modalTitle = 'Creacion de archivo';
  isCreationMode = false;
  hideFirstMessageControl = false;
  selectedFile: any;

  constructor(private fb: FormBuilder,
    private fileService: FileService,
    private toastNotificationService: ToastNotificationService,
    private accountService: AccountService) { }

  ngOnInit() {
    this.fileForm = this.fb.group({
      Name: ['', Validators.required],
      FirstMessage: ['', Validators.required],
      FileType: ['', Validators.required]
    });
    this.getFiles();
  }

  getFiles() {
    this.fileService.getFiles().then(files => {
      this.files = files;
    }).catch(() => {
      this.displayToast('Error', 'No se lograron cargar los archivos', ToastType.Error);
    });
  }

  showModalAsCreationMode(): void {
    this.isVisible = true;
    this.isCreationMode = true;
    this.hideFirstMessageControl = false;
  }

  showModalAsEditMode(data: any): void {
    this.selectedFile = data;
    this.isVisible = true;
    this.isCreationMode = false;
    this.fileForm.controls['FileType'].disable();
    this.hideFirstMessageControl = false;
    this.fileForm.patchValue({
      Name: data.Name,
      UserOwner: data.Owner,
      FileType: data.Type
    });
  }

  handleOk(): void {
    if (!this.fileForm.valid) {
      this.displayToast('Error', 'Se necesita llenar todos los campos', ToastType.Error);
    } else {
      if (this.isCreationMode) {
        const fileForm = this.fileForm.getRawValue();
        this.createFile(fileForm);
        this.isVisible = false;
        this.fileForm.reset();
      } else {
        const fileForm = this.fileForm.getRawValue();
        fileForm.UserOwner = this.accountService.currentUser.result.Username;
        const request = {
          Id: this.selectedFile.Id,
          Name: this.selectedFile.Name,
          FileType: this.selectedFile.Type
        };

        this.fileService.deleteFile(request).then(succes => {
          if (succes) {
            this.fileService.createFile(fileForm).then(() => {
              this.displayToast('Archivo actualizado', '', ToastType.Success);
              this.getFiles();
            });
          }
          this.getFiles();
        }).catch(() => {
          this.displayToast('Error', 'No se logro actualizar el archivo', ToastType.Error);
        });
        this.isVisible = false;
        this.fileForm.reset();
      }
    }
  }

  private createFile(fileForm: any) {
    fileForm.UserOwner = this.accountService.currentUser.result.Username;
    this.fileService.createFile(fileForm).then(() => {
      this.displayToast('Archivo creado', '', ToastType.Success);
      this.getFiles();
    }).catch(() => {
      this.displayToast('Error', 'No se logro crear el archivo', ToastType.Error);
    });
  }

  deleteFile(file) {
    const request = {
      Id: file.Id,
      Name: file.Name,
      FileType: file.Type
    };
    this.fileService.deleteFile(request).then(succes => {
      if (succes) {
        this.displayToast('Archivo eliminado', '', ToastType.Success);
      } else {
        this.displayToast('No se pudo eliminar el archivo', '', ToastType.Error);
      }
      this.getFiles();
    }).catch(() => {
      this.displayToast('Error', 'No se logro borrar el archivo', ToastType.Error);
    });
  }

  handleCancel(): void {
    this.isVisible = false;
  }

  displayToast(title: string, message: string, toastType: ToastType): void {
    this.toastNotificationService.show({
      title: title,
      message: message
    }, toastType);
  }

  canDelete(file) {
    return file.Owner.toLowerCase() === this.accountService.currentUser.result.Username;
  }
}
