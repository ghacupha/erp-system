import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouDepreciationEntryReportItemDetailComponent } from './rou-depreciation-entry-report-item-detail.component';

describe('RouDepreciationEntryReportItem Management Detail Component', () => {
  let comp: RouDepreciationEntryReportItemDetailComponent;
  let fixture: ComponentFixture<RouDepreciationEntryReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouDepreciationEntryReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouDepreciationEntryReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouDepreciationEntryReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouDepreciationEntryReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rouDepreciationEntryReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouDepreciationEntryReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
