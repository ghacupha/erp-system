import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrepaymentOutstandingOverviewReportDetailComponent } from './prepayment-outstanding-overview-report-detail.component';

describe('PrepaymentOutstandingOverviewReport Management Detail Component', () => {
  let comp: PrepaymentOutstandingOverviewReportDetailComponent;
  let fixture: ComponentFixture<PrepaymentOutstandingOverviewReportDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrepaymentOutstandingOverviewReportDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prepaymentOutstandingOverviewReport: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrepaymentOutstandingOverviewReportDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrepaymentOutstandingOverviewReportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prepaymentOutstandingOverviewReport on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prepaymentOutstandingOverviewReport).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
