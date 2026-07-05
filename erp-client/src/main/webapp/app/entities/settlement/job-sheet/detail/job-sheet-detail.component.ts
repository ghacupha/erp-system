import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJobSheet } from '../job-sheet.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-job-sheet-detail',
  templateUrl: './job-sheet-detail.component.html',
})
export class JobSheetDetailComponent implements OnInit {
  jobSheet: IJobSheet | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobSheet }) => {
      this.jobSheet = jobSheet;
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
