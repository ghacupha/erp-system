import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICardState } from '../card-state.model';

@Component({
  selector: 'jhi-card-state-detail',
  templateUrl: './card-state-detail.component.html',
})
export class CardStateDetailComponent implements OnInit {
  cardState: ICardState | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardState }) => {
      this.cardState = cardState;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
