import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIsoCurrencyCode } from '../iso-currency-code.model';

@Component({
  selector: 'jhi-iso-currency-code-detail',
  templateUrl: './iso-currency-code-detail.component.html',
})
export class IsoCurrencyCodeDetailComponent implements OnInit {
  isoCurrencyCode: IIsoCurrencyCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isoCurrencyCode }) => {
      this.isoCurrencyCode = isoCurrencyCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
