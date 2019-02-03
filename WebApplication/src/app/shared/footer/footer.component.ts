import { Component } from '@angular/core';

@Component({
    // tslint:disable-next-line:component-selector
    selector: 'footer-cmp',
    templateUrl: 'footer.component.html'
})

export class FooterComponent {
    test: Date = new Date();
}
