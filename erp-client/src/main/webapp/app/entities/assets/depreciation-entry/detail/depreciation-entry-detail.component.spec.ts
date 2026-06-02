import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DepreciationEntryDetailComponent } from './depreciation-entry-detail.component';

describe('DepreciationEntry Management Detail Component', () => {
  let comp: DepreciationEntryDetailComponent;
  let fixture: ComponentFixture<DepreciationEntryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepreciationEntryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ depreciationEntry: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DepreciationEntryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DepreciationEntryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load depreciationEntry on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.depreciationEntry).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
