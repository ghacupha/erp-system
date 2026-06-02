import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouAssetListReportItemDetailComponent } from './rou-asset-list-report-item-detail.component';

describe('RouAssetListReportItem Management Detail Component', () => {
  let comp: RouAssetListReportItemDetailComponent;
  let fixture: ComponentFixture<RouAssetListReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouAssetListReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouAssetListReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouAssetListReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouAssetListReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rouAssetListReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouAssetListReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
