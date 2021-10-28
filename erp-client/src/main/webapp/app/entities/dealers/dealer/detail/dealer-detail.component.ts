import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDealer } from '../dealer.model';

@Component({
  selector: 'jhi-dealer-detail',
  templateUrl: './dealer-detail.component.html',
})
export class DealerDetailComponent implements OnInit {
  dealer: IDealer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dealer }) => {
      this.dealer = dealer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
