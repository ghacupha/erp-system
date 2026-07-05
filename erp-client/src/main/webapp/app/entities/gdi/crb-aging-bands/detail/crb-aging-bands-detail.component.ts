import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbAgingBands } from '../crb-aging-bands.model';

@Component({
  selector: 'jhi-crb-aging-bands-detail',
  templateUrl: './crb-aging-bands-detail.component.html',
})
export class CrbAgingBandsDetailComponent implements OnInit {
  crbAgingBands: ICrbAgingBands | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbAgingBands }) => {
      this.crbAgingBands = crbAgingBands;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
