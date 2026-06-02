import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseAmortizationCalculationDetailComponent } from './lease-amortization-calculation-detail.component';

describe('LeaseAmortizationCalculation Management Detail Component', () => {
  let comp: LeaseAmortizationCalculationDetailComponent;
  let fixture: ComponentFixture<LeaseAmortizationCalculationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseAmortizationCalculationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseAmortizationCalculation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseAmortizationCalculationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseAmortizationCalculationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseAmortizationCalculation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseAmortizationCalculation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
