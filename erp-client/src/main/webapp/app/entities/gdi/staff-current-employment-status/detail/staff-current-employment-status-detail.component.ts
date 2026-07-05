import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStaffCurrentEmploymentStatus } from '../staff-current-employment-status.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-staff-current-employment-status-detail',
  templateUrl: './staff-current-employment-status-detail.component.html',
})
export class StaffCurrentEmploymentStatusDetailComponent implements OnInit {
  staffCurrentEmploymentStatus: IStaffCurrentEmploymentStatus | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staffCurrentEmploymentStatus }) => {
      this.staffCurrentEmploymentStatus = staffCurrentEmploymentStatus;
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
