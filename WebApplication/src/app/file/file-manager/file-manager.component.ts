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
  reportForm: FormGroup;
  modalTitle = 'Crear Estudiante';
  reportTittle = 'Crear Reporte en Word';
  isCreationMode = false;
  selectedFile = '';
  isVisibleReport = false;
  isVisiblePresentation = false;
  constructor(private fb: FormBuilder,
    private fileService: FileService,
    private toastNotificationService: ToastNotificationService) { }

  ngOnInit() {
    this.fileForm = this.fb.group({
      Name: ['', Validators.required],
      LastName: ['', Validators.required],
      UniqueId: ['', Validators.required],
      Age: ['', Validators.required],
      Session: ['', Validators.required]
    });
    this.reportForm = this.fb.group({
      Name: [''],
      Message: ['']
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

  showModalAsEditMode(file: string): void {
    this.selectedFile = file;
    this.isVisible = true;
    this.isCreationMode = false;
  }

  showReportModal(): void {
    this.isVisibleReport = true;
  }

  showPresentationModal(): void {
    this.isVisiblePresentation = true;
  }

  handleReport(): void {
    const word = this.reportForm.getRawValue();
    this.createWord(word);
    this.isVisibleReport = false;
    this.reportForm.reset();
  }

  handelReportCancel(): void {
    this.isVisibleReport = false;
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
        // const file = this.files.find(x => x.name == this.selectedFile);
        // console.log(this.files)
        // this.fileForm.setValue({
        //   name: file.name,
        //   firstMessage: file.firstMessage,
        //   userOwner: file.userOwner,
        //   fileType: file.fileType
        // });
      }
    }
  }

  private createFile(fileForm: any) {
    this.fileService.createFile(fileForm).then(() => {
      this.displayToast('El estudiante ha sido creado.', '', ToastType.Success);
      console.log(fileForm);
      this.getFiles();
    }).catch(() => {
      this.displayToast('Error', 'No se logro crear el archivo', ToastType.Error);
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

  private createWord(word) {
    this.fileService.createWordFile(word).then(() => {
      this.displayToast('El archivo ha sido creado', '', ToastType.Success);
      console.log(word);
    }).catch(() => {
      this.displayToast('Error', 'No se logro crear el archivo', ToastType.Error);
    });
  }

}
