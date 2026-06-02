import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MfbBranchCodeDetailComponent } from './mfb-branch-code-detail.component';

describe('MfbBranchCode Management Detail Component', () => {
  let comp: MfbBranchCodeDetailComponent;
  let fixture: ComponentFixture<MfbBranchCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MfbBranchCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ mfbBranchCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MfbBranchCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MfbBranchCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mfbBranchCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.mfbBranchCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
