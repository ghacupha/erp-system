import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISettlementRequisition } from '../settlement-requisition.model';

@Component({
  selector: 'jhi-settlement-requisition-detail',
  templateUrl: './settlement-requisition-detail.component.html',
})
export class SettlementRequisitionDetailComponent implements OnInit {
  settlementRequisition: ISettlementRequisition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ settlementRequisition }) => {
      this.settlementRequisition = settlementRequisition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
