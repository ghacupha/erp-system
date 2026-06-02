import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApplicationUserDetailComponent } from './application-user-detail.component';

describe('ApplicationUser Management Detail Component', () => {
  let comp: ApplicationUserDetailComponent;
  let fixture: ComponentFixture<ApplicationUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApplicationUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ applicationUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ApplicationUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ApplicationUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load applicationUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.applicationUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
