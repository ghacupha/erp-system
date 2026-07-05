import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReasonsForBouncedCheque } from '../reasons-for-bounced-cheque.model';

@Component({
  selector: 'jhi-reasons-for-bounced-cheque-detail',
  templateUrl: './reasons-for-bounced-cheque-detail.component.html',
})
export class ReasonsForBouncedChequeDetailComponent implements OnInit {
  reasonsForBouncedCheque: IReasonsForBouncedCheque | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reasonsForBouncedCheque }) => {
      this.reasonsForBouncedCheque = reasonsForBouncedCheque;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
