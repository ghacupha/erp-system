import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrepaymentAccountReportDetailComponent } from './prepayment-account-report-detail.component';

describe('PrepaymentAccountReport Management Detail Component', () => {
  let comp: PrepaymentAccountReportDetailComponent;
  let fixture: ComponentFixture<PrepaymentAccountReportDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrepaymentAccountReportDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prepaymentAccountReport: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrepaymentAccountReportDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrepaymentAccountReportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prepaymentAccountReport on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prepaymentAccountReport).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
