import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMfbBranchCode } from '../mfb-branch-code.model';

@Component({
  selector: 'jhi-mfb-branch-code-detail',
  templateUrl: './mfb-branch-code-detail.component.html',
})
export class MfbBranchCodeDetailComponent implements OnInit {
  mfbBranchCode: IMfbBranchCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mfbBranchCode }) => {
      this.mfbBranchCode = mfbBranchCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
