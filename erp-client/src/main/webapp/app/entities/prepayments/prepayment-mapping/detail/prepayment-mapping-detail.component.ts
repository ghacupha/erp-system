import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentMapping } from '../prepayment-mapping.model';

@Component({
  selector: 'jhi-prepayment-mapping-detail',
  templateUrl: './prepayment-mapping-detail.component.html',
})
export class PrepaymentMappingDetailComponent implements OnInit {
  prepaymentMapping: IPrepaymentMapping | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentMapping }) => {
      this.prepaymentMapping = prepaymentMapping;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
