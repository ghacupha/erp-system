import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProfessionalQualification } from '../professional-qualification.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-professional-qualification-detail',
  templateUrl: './professional-qualification-detail.component.html',
})
export class ProfessionalQualificationDetailComponent implements OnInit {
  professionalQualification: IProfessionalQualification | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ professionalQualification }) => {
      this.professionalQualification = professionalQualification;
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
