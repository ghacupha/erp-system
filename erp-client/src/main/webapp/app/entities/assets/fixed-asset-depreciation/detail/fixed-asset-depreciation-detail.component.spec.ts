import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FixedAssetDepreciationDetailComponent } from './fixed-asset-depreciation-detail.component';

describe('Component Tests', () => {
  describe('FixedAssetDepreciation Management Detail Component', () => {
    let comp: FixedAssetDepreciationDetailComponent;
    let fixture: ComponentFixture<FixedAssetDepreciationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FixedAssetDepreciationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fixedAssetDepreciation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FixedAssetDepreciationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FixedAssetDepreciationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fixedAssetDepreciation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fixedAssetDepreciation).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
