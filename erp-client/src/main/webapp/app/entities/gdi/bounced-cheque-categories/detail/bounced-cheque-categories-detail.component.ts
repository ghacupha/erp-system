import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBouncedChequeCategories } from '../bounced-cheque-categories.model';

@Component({
  selector: 'jhi-bounced-cheque-categories-detail',
  templateUrl: './bounced-cheque-categories-detail.component.html',
})
export class BouncedChequeCategoriesDetailComponent implements OnInit {
  bouncedChequeCategories: IBouncedChequeCategories | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bouncedChequeCategories }) => {
      this.bouncedChequeCategories = bouncedChequeCategories;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
