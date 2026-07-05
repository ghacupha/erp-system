import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbCreditApplicationStatus } from '../crb-credit-application-status.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-crb-credit-application-status-detail',
  templateUrl: './crb-credit-application-status-detail.component.html',
})
export class CrbCreditApplicationStatusDetailComponent implements OnInit {
  crbCreditApplicationStatus: ICrbCreditApplicationStatus | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbCreditApplicationStatus }) => {
      this.crbCreditApplicationStatus = crbCreditApplicationStatus;
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
