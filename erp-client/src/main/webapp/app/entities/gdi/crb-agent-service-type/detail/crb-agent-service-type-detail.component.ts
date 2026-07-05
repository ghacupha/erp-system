import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbAgentServiceType } from '../crb-agent-service-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-crb-agent-service-type-detail',
  templateUrl: './crb-agent-service-type-detail.component.html',
})
export class CrbAgentServiceTypeDetailComponent implements OnInit {
  crbAgentServiceType: ICrbAgentServiceType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbAgentServiceType }) => {
      this.crbAgentServiceType = crbAgentServiceType;
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
