import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICardUsageInformation } from '../card-usage-information.model';

@Component({
  selector: 'jhi-card-usage-information-detail',
  templateUrl: './card-usage-information-detail.component.html',
})
export class CardUsageInformationDetailComponent implements OnInit {
  cardUsageInformation: ICardUsageInformation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardUsageInformation }) => {
      this.cardUsageInformation = cardUsageInformation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
