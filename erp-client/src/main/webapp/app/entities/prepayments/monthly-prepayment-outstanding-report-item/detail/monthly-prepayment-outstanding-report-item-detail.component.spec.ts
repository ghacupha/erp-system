import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MonthlyPrepaymentOutstandingReportItemDetailComponent } from './monthly-prepayment-outstanding-report-item-detail.component';

describe('MonthlyPrepaymentOutstandingReportItem Management Detail Component', () => {
  let comp: MonthlyPrepaymentOutstandingReportItemDetailComponent;
  let fixture: ComponentFixture<MonthlyPrepaymentOutstandingReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MonthlyPrepaymentOutstandingReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ monthlyPrepaymentOutstandingReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MonthlyPrepaymentOutstandingReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MonthlyPrepaymentOutstandingReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load monthlyPrepaymentOutstandingReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.monthlyPrepaymentOutstandingReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
