import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUltimateBeneficiaryCategory } from '../ultimate-beneficiary-category.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-ultimate-beneficiary-category-detail',
  templateUrl: './ultimate-beneficiary-category-detail.component.html',
})
export class UltimateBeneficiaryCategoryDetailComponent implements OnInit {
  ultimateBeneficiaryCategory: IUltimateBeneficiaryCategory | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ultimateBeneficiaryCategory }) => {
      this.ultimateBeneficiaryCategory = ultimateBeneficiaryCategory;
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
