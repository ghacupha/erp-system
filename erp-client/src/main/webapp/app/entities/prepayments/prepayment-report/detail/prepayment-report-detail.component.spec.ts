import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrepaymentReportDetailComponent } from './prepayment-report-detail.component';

describe('PrepaymentReport Management Detail Component', () => {
  let comp: PrepaymentReportDetailComponent;
  let fixture: ComponentFixture<PrepaymentReportDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrepaymentReportDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prepaymentReport: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrepaymentReportDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrepaymentReportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prepaymentReport on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prepaymentReport).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
