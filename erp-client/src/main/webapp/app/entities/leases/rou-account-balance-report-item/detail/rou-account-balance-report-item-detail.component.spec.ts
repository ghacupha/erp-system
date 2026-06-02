import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouAccountBalanceReportItemDetailComponent } from './rou-account-balance-report-item-detail.component';

describe('RouAccountBalanceReportItem Management Detail Component', () => {
  let comp: RouAccountBalanceReportItemDetailComponent;
  let fixture: ComponentFixture<RouAccountBalanceReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouAccountBalanceReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouAccountBalanceReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouAccountBalanceReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouAccountBalanceReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rouAccountBalanceReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouAccountBalanceReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
