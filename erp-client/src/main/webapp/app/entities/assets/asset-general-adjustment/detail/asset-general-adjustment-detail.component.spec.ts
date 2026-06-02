import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssetGeneralAdjustmentDetailComponent } from './asset-general-adjustment-detail.component';

describe('AssetGeneralAdjustment Management Detail Component', () => {
  let comp: AssetGeneralAdjustmentDetailComponent;
  let fixture: ComponentFixture<AssetGeneralAdjustmentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetGeneralAdjustmentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assetGeneralAdjustment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssetGeneralAdjustmentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetGeneralAdjustmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assetGeneralAdjustment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assetGeneralAdjustment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
