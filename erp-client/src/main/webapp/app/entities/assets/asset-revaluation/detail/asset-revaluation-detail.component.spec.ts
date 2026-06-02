import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssetRevaluationDetailComponent } from './asset-revaluation-detail.component';

describe('AssetRevaluation Management Detail Component', () => {
  let comp: AssetRevaluationDetailComponent;
  let fixture: ComponentFixture<AssetRevaluationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetRevaluationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assetRevaluation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssetRevaluationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetRevaluationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assetRevaluation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assetRevaluation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
