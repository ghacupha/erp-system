import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICardFraudIncidentCategory } from '../card-fraud-incident-category.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-card-fraud-incident-category-detail',
  templateUrl: './card-fraud-incident-category-detail.component.html',
})
export class CardFraudIncidentCategoryDetailComponent implements OnInit {
  cardFraudIncidentCategory: ICardFraudIncidentCategory | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardFraudIncidentCategory }) => {
      this.cardFraudIncidentCategory = cardFraudIncidentCategory;
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
