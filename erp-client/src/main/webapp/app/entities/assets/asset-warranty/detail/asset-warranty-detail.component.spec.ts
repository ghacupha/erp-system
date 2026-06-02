import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssetWarrantyDetailComponent } from './asset-warranty-detail.component';

describe('AssetWarranty Management Detail Component', () => {
  let comp: AssetWarrantyDetailComponent;
  let fixture: ComponentFixture<AssetWarrantyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetWarrantyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assetWarranty: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssetWarrantyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetWarrantyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assetWarranty on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assetWarranty).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
