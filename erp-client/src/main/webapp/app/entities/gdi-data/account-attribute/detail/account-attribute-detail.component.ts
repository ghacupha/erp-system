import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccountAttribute } from '../account-attribute.model';

@Component({
  selector: 'jhi-account-attribute-detail',
  templateUrl: './account-attribute-detail.component.html',
})
export class AccountAttributeDetailComponent implements OnInit {
  accountAttribute: IAccountAttribute | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountAttribute }) => {
      this.accountAttribute = accountAttribute;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
