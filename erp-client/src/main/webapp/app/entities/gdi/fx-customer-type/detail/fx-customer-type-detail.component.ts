import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFxCustomerType } from '../fx-customer-type.model';

@Component({
  selector: 'jhi-fx-customer-type-detail',
  templateUrl: './fx-customer-type-detail.component.html',
})
export class FxCustomerTypeDetailComponent implements OnInit {
  fxCustomerType: IFxCustomerType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fxCustomerType }) => {
      this.fxCustomerType = fxCustomerType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
