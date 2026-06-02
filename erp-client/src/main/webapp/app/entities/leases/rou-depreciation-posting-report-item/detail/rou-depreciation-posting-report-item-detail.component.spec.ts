import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouDepreciationPostingReportItemDetailComponent } from './rou-depreciation-posting-report-item-detail.component';

describe('RouDepreciationPostingReportItem Management Detail Component', () => {
  let comp: RouDepreciationPostingReportItemDetailComponent;
  let fixture: ComponentFixture<RouDepreciationPostingReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouDepreciationPostingReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouDepreciationPostingReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouDepreciationPostingReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouDepreciationPostingReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rouDepreciationPostingReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouDepreciationPostingReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
