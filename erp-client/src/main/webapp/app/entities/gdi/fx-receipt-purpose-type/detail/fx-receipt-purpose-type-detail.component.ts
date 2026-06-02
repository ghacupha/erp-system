import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFxReceiptPurposeType } from '../fx-receipt-purpose-type.model';

@Component({
  selector: 'jhi-fx-receipt-purpose-type-detail',
  templateUrl: './fx-receipt-purpose-type-detail.component.html',
})
export class FxReceiptPurposeTypeDetailComponent implements OnInit {
  fxReceiptPurposeType: IFxReceiptPurposeType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fxReceiptPurposeType }) => {
      this.fxReceiptPurposeType = fxReceiptPurposeType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
