import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAgencyNotice } from '../agency-notice.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-agency-notice-detail',
  templateUrl: './agency-notice-detail.component.html',
})
export class AgencyNoticeDetailComponent implements OnInit {
  agencyNotice: IAgencyNotice | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agencyNotice }) => {
      this.agencyNotice = agencyNotice;
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
