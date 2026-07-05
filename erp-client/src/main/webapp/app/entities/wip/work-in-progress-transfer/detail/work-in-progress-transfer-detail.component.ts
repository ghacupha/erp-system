import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkInProgressTransfer } from '../work-in-progress-transfer.model';

@Component({
  selector: 'jhi-work-in-progress-transfer-detail',
  templateUrl: './work-in-progress-transfer-detail.component.html',
})
export class WorkInProgressTransferDetailComponent implements OnInit {
  workInProgressTransfer: IWorkInProgressTransfer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workInProgressTransfer }) => {
      this.workInProgressTransfer = workInProgressTransfer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
