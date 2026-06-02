import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DepreciationBatchSequenceDetailComponent } from './depreciation-batch-sequence-detail.component';

describe('DepreciationBatchSequence Management Detail Component', () => {
  let comp: DepreciationBatchSequenceDetailComponent;
  let fixture: ComponentFixture<DepreciationBatchSequenceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepreciationBatchSequenceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ depreciationBatchSequence: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DepreciationBatchSequenceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DepreciationBatchSequenceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load depreciationBatchSequence on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.depreciationBatchSequence).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
