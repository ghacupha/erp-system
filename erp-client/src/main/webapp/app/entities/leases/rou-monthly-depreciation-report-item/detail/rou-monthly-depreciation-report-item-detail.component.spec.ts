import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouMonthlyDepreciationReportItemDetailComponent } from './rou-monthly-depreciation-report-item-detail.component';

describe('RouMonthlyDepreciationReportItem Management Detail Component', () => {
  let comp: RouMonthlyDepreciationReportItemDetailComponent;
  let fixture: ComponentFixture<RouMonthlyDepreciationReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouMonthlyDepreciationReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouMonthlyDepreciationReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouMonthlyDepreciationReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouMonthlyDepreciationReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rouMonthlyDepreciationReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouMonthlyDepreciationReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
