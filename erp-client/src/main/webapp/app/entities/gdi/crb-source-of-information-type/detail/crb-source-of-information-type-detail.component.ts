import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbSourceOfInformationType } from '../crb-source-of-information-type.model';

@Component({
  selector: 'jhi-crb-source-of-information-type-detail',
  templateUrl: './crb-source-of-information-type-detail.component.html',
})
export class CrbSourceOfInformationTypeDetailComponent implements OnInit {
  crbSourceOfInformationType: ICrbSourceOfInformationType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbSourceOfInformationType }) => {
      this.crbSourceOfInformationType = crbSourceOfInformationType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
