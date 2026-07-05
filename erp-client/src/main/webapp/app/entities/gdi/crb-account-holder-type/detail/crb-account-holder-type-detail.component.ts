import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbAccountHolderType } from '../crb-account-holder-type.model';

@Component({
  selector: 'jhi-crb-account-holder-type-detail',
  templateUrl: './crb-account-holder-type-detail.component.html',
})
export class CrbAccountHolderTypeDetailComponent implements OnInit {
  crbAccountHolderType: ICrbAccountHolderType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbAccountHolderType }) => {
      this.crbAccountHolderType = crbAccountHolderType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
