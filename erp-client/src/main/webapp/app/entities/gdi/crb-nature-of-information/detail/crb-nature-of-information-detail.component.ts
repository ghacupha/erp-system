import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbNatureOfInformation } from '../crb-nature-of-information.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-crb-nature-of-information-detail',
  templateUrl: './crb-nature-of-information-detail.component.html',
})
export class CrbNatureOfInformationDetailComponent implements OnInit {
  crbNatureOfInformation: ICrbNatureOfInformation | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbNatureOfInformation }) => {
      this.crbNatureOfInformation = crbNatureOfInformation;
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
