import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract.model';

@Component({
  selector: 'jhi-ifrs-16-lease-contract-detail',
  templateUrl: './ifrs-16-lease-contract-detail.component.html',
})
export class IFRS16LeaseContractDetailComponent implements OnInit {
  iFRS16LeaseContract: IIFRS16LeaseContract | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ iFRS16LeaseContract }) => {
      this.iFRS16LeaseContract = iFRS16LeaseContract;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
