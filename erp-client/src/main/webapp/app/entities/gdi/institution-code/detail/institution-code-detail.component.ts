import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInstitutionCode } from '../institution-code.model';

@Component({
  selector: 'jhi-institution-code-detail',
  templateUrl: './institution-code-detail.component.html',
})
export class InstitutionCodeDetailComponent implements OnInit {
  institutionCode: IInstitutionCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ institutionCode }) => {
      this.institutionCode = institutionCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
