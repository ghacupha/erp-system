import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MessageTokenDetailComponent } from './message-token-detail.component';

describe('MessageToken Management Detail Component', () => {
  let comp: MessageTokenDetailComponent;
  let fixture: ComponentFixture<MessageTokenDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MessageTokenDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ messageToken: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MessageTokenDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MessageTokenDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load messageToken on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.messageToken).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
