import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CrbDataSubmittingInstitutionsDetailComponent } from './crb-data-submitting-institutions-detail.component';

describe('CrbDataSubmittingInstitutions Management Detail Component', () => {
  let comp: CrbDataSubmittingInstitutionsDetailComponent;
  let fixture: ComponentFixture<CrbDataSubmittingInstitutionsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CrbDataSubmittingInstitutionsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ crbDataSubmittingInstitutions: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CrbDataSubmittingInstitutionsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CrbDataSubmittingInstitutionsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load crbDataSubmittingInstitutions on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.crbDataSubmittingInstitutions).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
