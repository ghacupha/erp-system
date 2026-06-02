import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FixedAssetAcquisitionDetailComponent } from './fixed-asset-acquisition-detail.component';

describe('FixedAssetAcquisition Management Detail Component', () => {
  let comp: FixedAssetAcquisitionDetailComponent;
  let fixture: ComponentFixture<FixedAssetAcquisitionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FixedAssetAcquisitionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fixedAssetAcquisition: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FixedAssetAcquisitionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FixedAssetAcquisitionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fixedAssetAcquisition on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fixedAssetAcquisition).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
