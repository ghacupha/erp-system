import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AmortizationPeriodDetailComponent } from './amortization-period-detail.component';

describe('AmortizationPeriod Management Detail Component', () => {
  let comp: AmortizationPeriodDetailComponent;
  let fixture: ComponentFixture<AmortizationPeriodDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AmortizationPeriodDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ amortizationPeriod: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AmortizationPeriodDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AmortizationPeriodDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load amortizationPeriod on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.amortizationPeriod).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
