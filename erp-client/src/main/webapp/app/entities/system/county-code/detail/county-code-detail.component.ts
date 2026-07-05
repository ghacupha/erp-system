import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICountyCode } from '../county-code.model';

@Component({
  selector: 'jhi-county-code-detail',
  templateUrl: './county-code-detail.component.html',
})
export class CountyCodeDetailComponent implements OnInit {
  countyCode: ICountyCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyCode }) => {
      this.countyCode = countyCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
