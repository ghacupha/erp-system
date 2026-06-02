import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouDepreciationEntryDetailComponent } from './rou-depreciation-entry-detail.component';

describe('RouDepreciationEntry Management Detail Component', () => {
  let comp: RouDepreciationEntryDetailComponent;
  let fixture: ComponentFixture<RouDepreciationEntryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouDepreciationEntryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouDepreciationEntry: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouDepreciationEntryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouDepreciationEntryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rouDepreciationEntry on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouDepreciationEntry).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
