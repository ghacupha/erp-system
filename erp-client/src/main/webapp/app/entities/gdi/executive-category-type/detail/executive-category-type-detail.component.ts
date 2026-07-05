import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExecutiveCategoryType } from '../executive-category-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-executive-category-type-detail',
  templateUrl: './executive-category-type-detail.component.html',
})
export class ExecutiveCategoryTypeDetailComponent implements OnInit {
  executiveCategoryType: IExecutiveCategoryType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ executiveCategoryType }) => {
      this.executiveCategoryType = executiveCategoryType;
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
