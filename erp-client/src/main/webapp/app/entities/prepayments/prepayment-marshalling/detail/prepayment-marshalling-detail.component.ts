import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentMarshalling } from '../prepayment-marshalling.model';

@Component({
  selector: 'jhi-prepayment-marshalling-detail',
  templateUrl: './prepayment-marshalling-detail.component.html',
})
export class PrepaymentMarshallingDetailComponent implements OnInit {
  prepaymentMarshalling: IPrepaymentMarshalling | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentMarshalling }) => {
      this.prepaymentMarshalling = prepaymentMarshalling;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
