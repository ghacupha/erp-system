import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouAssetNBVReportItemDetailComponent } from './rou-asset-nbv-report-item-detail.component';

describe('RouAssetNBVReportItem Management Detail Component', () => {
  let comp: RouAssetNBVReportItemDetailComponent;
  let fixture: ComponentFixture<RouAssetNBVReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouAssetNBVReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouAssetNBVReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouAssetNBVReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouAssetNBVReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rouAssetNBVReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouAssetNBVReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
