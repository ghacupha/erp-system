import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbProductServiceFeeType } from '../crb-product-service-fee-type.model';

@Component({
  selector: 'jhi-crb-product-service-fee-type-detail',
  templateUrl: './crb-product-service-fee-type-detail.component.html',
})
export class CrbProductServiceFeeTypeDetailComponent implements OnInit {
  crbProductServiceFeeType: ICrbProductServiceFeeType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbProductServiceFeeType }) => {
      this.crbProductServiceFeeType = crbProductServiceFeeType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
