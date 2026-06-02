import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RelatedPartyRelationshipDetailComponent } from './related-party-relationship-detail.component';

describe('RelatedPartyRelationship Management Detail Component', () => {
  let comp: RelatedPartyRelationshipDetailComponent;
  let fixture: ComponentFixture<RelatedPartyRelationshipDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RelatedPartyRelationshipDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ relatedPartyRelationship: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RelatedPartyRelationshipDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RelatedPartyRelationshipDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load relatedPartyRelationship on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.relatedPartyRelationship).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
