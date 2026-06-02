import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssetDisposalDetailComponent } from './asset-disposal-detail.component';

describe('AssetDisposal Management Detail Component', () => {
  let comp: AssetDisposalDetailComponent;
  let fixture: ComponentFixture<AssetDisposalDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetDisposalDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assetDisposal: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssetDisposalDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetDisposalDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assetDisposal on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assetDisposal).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
