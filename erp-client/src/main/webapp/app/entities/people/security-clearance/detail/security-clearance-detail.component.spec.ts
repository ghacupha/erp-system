import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SecurityClearanceDetailComponent } from './security-clearance-detail.component';

describe('SecurityClearance Management Detail Component', () => {
  let comp: SecurityClearanceDetailComponent;
  let fixture: ComponentFixture<SecurityClearanceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SecurityClearanceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ securityClearance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SecurityClearanceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SecurityClearanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load securityClearance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.securityClearance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
