import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnticipatedMaturityPeriood } from '../anticipated-maturity-periood.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-anticipated-maturity-periood-detail',
  templateUrl: './anticipated-maturity-periood-detail.component.html',
})
export class AnticipatedMaturityPerioodDetailComponent implements OnInit {
  anticipatedMaturityPeriood: IAnticipatedMaturityPeriood | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ anticipatedMaturityPeriood }) => {
      this.anticipatedMaturityPeriood = anticipatedMaturityPeriood;
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
