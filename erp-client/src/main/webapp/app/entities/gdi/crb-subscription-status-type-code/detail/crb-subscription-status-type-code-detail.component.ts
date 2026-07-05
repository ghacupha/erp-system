import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbSubscriptionStatusTypeCode } from '../crb-subscription-status-type-code.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-crb-subscription-status-type-code-detail',
  templateUrl: './crb-subscription-status-type-code-detail.component.html',
})
export class CrbSubscriptionStatusTypeCodeDetailComponent implements OnInit {
  crbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbSubscriptionStatusTypeCode }) => {
      this.crbSubscriptionStatusTypeCode = crbSubscriptionStatusTypeCode;
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
