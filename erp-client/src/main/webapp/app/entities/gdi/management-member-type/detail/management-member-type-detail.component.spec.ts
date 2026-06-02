import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ManagementMemberTypeDetailComponent } from './management-member-type-detail.component';

describe('ManagementMemberType Management Detail Component', () => {
  let comp: ManagementMemberTypeDetailComponent;
  let fixture: ComponentFixture<ManagementMemberTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManagementMemberTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ managementMemberType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ManagementMemberTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ManagementMemberTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load managementMemberType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.managementMemberType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
