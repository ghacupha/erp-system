import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICardFraudInformation } from '../card-fraud-information.model';

@Component({
  selector: 'jhi-card-fraud-information-detail',
  templateUrl: './card-fraud-information-detail.component.html',
})
export class CardFraudInformationDetailComponent implements OnInit {
  cardFraudInformation: ICardFraudInformation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardFraudInformation }) => {
      this.cardFraudInformation = cardFraudInformation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
