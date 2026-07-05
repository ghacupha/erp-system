import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseContract } from '../lease-contract.model';

@Component({
  selector: 'jhi-lease-contract-detail',
  templateUrl: './lease-contract-detail.component.html',
})
export class LeaseContractDetailComponent implements OnInit {
  leaseContract: ILeaseContract | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseContract }) => {
      this.leaseContract = leaseContract;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
