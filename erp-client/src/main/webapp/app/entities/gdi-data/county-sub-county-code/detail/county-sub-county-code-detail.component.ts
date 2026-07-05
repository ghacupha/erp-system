import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICountySubCountyCode } from '../county-sub-county-code.model';

@Component({
  selector: 'jhi-county-sub-county-code-detail',
  templateUrl: './county-sub-county-code-detail.component.html',
})
export class CountySubCountyCodeDetailComponent implements OnInit {
  countySubCountyCode: ICountySubCountyCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countySubCountyCode }) => {
      this.countySubCountyCode = countySubCountyCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
