import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubCountyCode } from '../sub-county-code.model';

@Component({
  selector: 'jhi-sub-county-code-detail',
  templateUrl: './sub-county-code-detail.component.html',
})
export class SubCountyCodeDetailComponent implements OnInit {
  subCountyCode: ISubCountyCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subCountyCode }) => {
      this.subCountyCode = subCountyCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
