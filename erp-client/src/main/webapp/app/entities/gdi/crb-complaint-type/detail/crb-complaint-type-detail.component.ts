import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbComplaintType } from '../crb-complaint-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-crb-complaint-type-detail',
  templateUrl: './crb-complaint-type-detail.component.html',
})
export class CrbComplaintTypeDetailComponent implements OnInit {
  crbComplaintType: ICrbComplaintType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbComplaintType }) => {
      this.crbComplaintType = crbComplaintType;
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
