import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICardIssuerCharges } from '../card-issuer-charges.model';

@Component({
  selector: 'jhi-card-issuer-charges-detail',
  templateUrl: './card-issuer-charges-detail.component.html',
})
export class CardIssuerChargesDetailComponent implements OnInit {
  cardIssuerCharges: ICardIssuerCharges | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardIssuerCharges }) => {
      this.cardIssuerCharges = cardIssuerCharges;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
