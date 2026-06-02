import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SystemModuleDetailComponent } from './system-module-detail.component';

describe('SystemModule Management Detail Component', () => {
  let comp: SystemModuleDetailComponent;
  let fixture: ComponentFixture<SystemModuleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SystemModuleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ systemModule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SystemModuleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SystemModuleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load systemModule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.systemModule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
