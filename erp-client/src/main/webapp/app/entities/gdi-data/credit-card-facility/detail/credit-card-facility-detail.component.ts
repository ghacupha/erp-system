import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICreditCardFacility } from '../credit-card-facility.model';

@Component({
  selector: 'jhi-credit-card-facility-detail',
  templateUrl: './credit-card-facility-detail.component.html',
})
export class CreditCardFacilityDetailComponent implements OnInit {
  creditCardFacility: ICreditCardFacility | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ creditCardFacility }) => {
      this.creditCardFacility = creditCardFacility;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
