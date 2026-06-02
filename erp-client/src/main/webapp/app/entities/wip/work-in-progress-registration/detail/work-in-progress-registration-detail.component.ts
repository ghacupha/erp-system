import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkInProgressRegistration } from '../work-in-progress-registration.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-work-in-progress-registration-detail',
  templateUrl: './work-in-progress-registration-detail.component.html',
})
export class WorkInProgressRegistrationDetailComponent implements OnInit {
  workInProgressRegistration: IWorkInProgressRegistration | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workInProgressRegistration }) => {
      this.workInProgressRegistration = workInProgressRegistration;
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
