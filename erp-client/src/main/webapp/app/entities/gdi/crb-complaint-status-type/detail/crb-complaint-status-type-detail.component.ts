import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbComplaintStatusType } from '../crb-complaint-status-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-crb-complaint-status-type-detail',
  templateUrl: './crb-complaint-status-type-detail.component.html',
})
export class CrbComplaintStatusTypeDetailComponent implements OnInit {
  crbComplaintStatusType: ICrbComplaintStatusType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbComplaintStatusType }) => {
      this.crbComplaintStatusType = crbComplaintStatusType;
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
