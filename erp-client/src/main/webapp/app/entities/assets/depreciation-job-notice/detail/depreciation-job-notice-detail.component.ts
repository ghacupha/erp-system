import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepreciationJobNotice } from '../depreciation-job-notice.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-depreciation-job-notice-detail',
  templateUrl: './depreciation-job-notice-detail.component.html',
})
export class DepreciationJobNoticeDetailComponent implements OnInit {
  depreciationJobNotice: IDepreciationJobNotice | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depreciationJobNotice }) => {
      this.depreciationJobNotice = depreciationJobNotice;
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
