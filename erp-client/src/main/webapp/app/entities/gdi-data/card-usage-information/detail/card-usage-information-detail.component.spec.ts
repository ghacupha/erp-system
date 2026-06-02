import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CardUsageInformationDetailComponent } from './card-usage-information-detail.component';

describe('CardUsageInformation Management Detail Component', () => {
  let comp: CardUsageInformationDetailComponent;
  let fixture: ComponentFixture<CardUsageInformationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CardUsageInformationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cardUsageInformation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CardUsageInformationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CardUsageInformationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cardUsageInformation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cardUsageInformation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
