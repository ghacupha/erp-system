import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseLiability } from '../lease-liability.model';

@Component({
  selector: 'jhi-lease-liability-detail',
  templateUrl: './lease-liability-detail.component.html',
})
export class LeaseLiabilityDetailComponent implements OnInit {
  leaseLiability: ILeaseLiability | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiability }) => {
      this.leaseLiability = leaseLiability;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
