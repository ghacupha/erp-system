import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInterestCalcMethod } from '../interest-calc-method.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-interest-calc-method-detail',
  templateUrl: './interest-calc-method-detail.component.html',
})
export class InterestCalcMethodDetailComponent implements OnInit {
  interestCalcMethod: IInterestCalcMethod | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interestCalcMethod }) => {
      this.interestCalcMethod = interestCalcMethod;
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
