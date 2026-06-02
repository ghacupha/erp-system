import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FixedAssetNetBookValueDetailComponent } from './fixed-asset-net-book-value-detail.component';

describe('FixedAssetNetBookValue Management Detail Component', () => {
  let comp: FixedAssetNetBookValueDetailComponent;
  let fixture: ComponentFixture<FixedAssetNetBookValueDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FixedAssetNetBookValueDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fixedAssetNetBookValue: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FixedAssetNetBookValueDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FixedAssetNetBookValueDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fixedAssetNetBookValue on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fixedAssetNetBookValue).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
