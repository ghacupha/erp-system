import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepreciationBatchSequence } from '../depreciation-batch-sequence.model';

@Component({
  selector: 'jhi-depreciation-batch-sequence-detail',
  templateUrl: './depreciation-batch-sequence-detail.component.html',
})
export class DepreciationBatchSequenceDetailComponent implements OnInit {
  depreciationBatchSequence: IDepreciationBatchSequence | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depreciationBatchSequence }) => {
      this.depreciationBatchSequence = depreciationBatchSequence;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
