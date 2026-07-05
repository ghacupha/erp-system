import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerIDDocumentType } from '../customer-id-document-type.model';

@Component({
  selector: 'jhi-customer-id-document-type-detail',
  templateUrl: './customer-id-document-type-detail.component.html',
})
export class CustomerIDDocumentTypeDetailComponent implements OnInit {
  customerIDDocumentType: ICustomerIDDocumentType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerIDDocumentType }) => {
      this.customerIDDocumentType = customerIDDocumentType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
