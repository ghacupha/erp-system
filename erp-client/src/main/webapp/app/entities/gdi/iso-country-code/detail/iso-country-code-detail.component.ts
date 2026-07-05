import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIsoCountryCode } from '../iso-country-code.model';

@Component({
  selector: 'jhi-iso-country-code-detail',
  templateUrl: './iso-country-code-detail.component.html',
})
export class IsoCountryCodeDetailComponent implements OnInit {
  isoCountryCode: IIsoCountryCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isoCountryCode }) => {
      this.isoCountryCode = isoCountryCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
