import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CardFraudInformationDetailComponent } from './card-fraud-information-detail.component';

describe('CardFraudInformation Management Detail Component', () => {
  let comp: CardFraudInformationDetailComponent;
  let fixture: ComponentFixture<CardFraudInformationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CardFraudInformationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cardFraudInformation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CardFraudInformationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CardFraudInformationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cardFraudInformation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cardFraudInformation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
