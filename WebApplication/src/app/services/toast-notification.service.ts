import { Injectable } from '@angular/core';
import { NzNotificationService } from 'ng-zorro-antd';

/**
 * Implements the functionality to interact with the Toast notification component
 *
 * @export
 */
@Injectable()
export class ToastNotificationService {

  private methods = [];
  constructor(private notification: NzNotificationService) {
    this.methods = [
      this.showSuccess.bind(this),
      this.showInfo.bind(this),
      this.showWarning.bind(this),
      this.showError.bind(this),
      this.showCustom.bind(this)
    ];
  }

  /**
   * Performs the action to show a message through a toast component with given characteristics
   */
  show(toast: ToastModel, type: ToastType) {
    this.methods[type](toast);
  }

  private showSuccess(toast: ToastModel) {
    this.notification.success(toast.title, toast.message, toast.options);
  }

  private showError(toast: ToastModel) {
    this.notification.error(toast.title, toast.message, toast.options);
  }

  private showWarning(toast: ToastModel) {
    this.notification.warning(toast.title, toast.message, toast.options);
  }

  private showInfo(toast: ToastModel) {
    this.notification.info(toast.title, toast.message, toast.options);
  }

  private showCustom(toast: ToastModel, notification: NzNotificationService) {
    notification.blank(toast.title, toast.message, toast.options);
  }

}
/**
 * Represents toast message types
 *
 * @export
 */
export enum ToastType {
  Success = 0,
  Info,
  Warning,
  Error,
  Custom
}

export class ToastModel {

  title: string;
  message: string;
  options?: any;
}

