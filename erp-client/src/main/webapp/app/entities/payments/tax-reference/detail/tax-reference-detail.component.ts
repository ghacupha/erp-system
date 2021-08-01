import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITaxReference } from '../tax-reference.model';

@Component({
  selector: 'jhi-tax-reference-detail',
  templateUrl: './tax-reference-detail.component.html',
})
export class TaxReferenceDetailComponent implements OnInit {
  taxReference: ITaxReference | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxReference }) => {
      this.taxReference = taxReference;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
