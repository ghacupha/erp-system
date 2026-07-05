import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAcquiringIssuingFlag } from '../acquiring-issuing-flag.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-acquiring-issuing-flag-detail',
  templateUrl: './acquiring-issuing-flag-detail.component.html',
})
export class AcquiringIssuingFlagDetailComponent implements OnInit {
  acquiringIssuingFlag: IAcquiringIssuingFlag | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acquiringIssuingFlag }) => {
      this.acquiringIssuingFlag = acquiringIssuingFlag;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
