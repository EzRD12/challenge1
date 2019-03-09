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
  selectedFile = '';

  constructor(private fb: FormBuilder,
    private fileService: FileService,
    private toastNotificationService: ToastNotificationService) { }

  ngOnInit() {
    this.fileForm = this.fb.group({
      Name: ['', Validators.required],
      FirstMessage: ['', Validators.required],
      UserOwner: ['', Validators.required],
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
  }

  showModalAsEditMode(data: any): void {
    console.log(data)
    this.selectedFile = 'fileName';
    this.isVisible = true;
    this.isCreationMode = false;
    this.fileForm.controls['FileType'].disable();
    const file = this.files.find(x => x.name == this.selectedFile);
    console.log(file);
    console.log(this.files);
    this.fileForm.setValue({
      name: file.name,
      firstMessage: file.firstMessage,
      userOwner: file.userOwner,
      fileType: file.fileType
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
        console.log('Updating...')
      }
    }
  }

  private createFile(fileForm: any) {
    this.fileService.createFile(fileForm).then(() => {
      this.displayToast('Archivo creado', '', ToastType.Success);
      this.getFiles();
    }).catch(() => {
      this.displayToast('Error', 'No se logro crear el archivo', ToastType.Error);
    });
  }

  deleteFile(file) {
    const request = {
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
}
