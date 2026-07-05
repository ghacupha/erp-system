import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICardStatusFlag } from '../card-status-flag.model';

@Component({
  selector: 'jhi-card-status-flag-detail',
  templateUrl: './card-status-flag-detail.component.html',
})
export class CardStatusFlagDetailComponent implements OnInit {
  cardStatusFlag: ICardStatusFlag | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardStatusFlag }) => {
      this.cardStatusFlag = cardStatusFlag;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
