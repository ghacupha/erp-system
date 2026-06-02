import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProcessStatusDetailComponent } from './process-status-detail.component';

describe('ProcessStatus Management Detail Component', () => {
  let comp: ProcessStatusDetailComponent;
  let fixture: ComponentFixture<ProcessStatusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcessStatusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ processStatus: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProcessStatusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProcessStatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load processStatus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.processStatus).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
