import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseAmortizationScheduleDetailComponent } from './lease-amortization-schedule-detail.component';

describe('LeaseAmortizationSchedule Management Detail Component', () => {
  let comp: LeaseAmortizationScheduleDetailComponent;
  let fixture: ComponentFixture<LeaseAmortizationScheduleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseAmortizationScheduleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseAmortizationSchedule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseAmortizationScheduleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseAmortizationScheduleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseAmortizationSchedule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseAmortizationSchedule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
