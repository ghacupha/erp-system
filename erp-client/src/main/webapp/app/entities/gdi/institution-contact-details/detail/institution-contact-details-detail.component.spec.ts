import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InstitutionContactDetailsDetailComponent } from './institution-contact-details-detail.component';

describe('InstitutionContactDetails Management Detail Component', () => {
  let comp: InstitutionContactDetailsDetailComponent;
  let fixture: ComponentFixture<InstitutionContactDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InstitutionContactDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ institutionContactDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InstitutionContactDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InstitutionContactDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load institutionContactDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.institutionContactDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
