import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AgentBankingActivityDetailComponent } from './agent-banking-activity-detail.component';

describe('AgentBankingActivity Management Detail Component', () => {
  let comp: AgentBankingActivityDetailComponent;
  let fixture: ComponentFixture<AgentBankingActivityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AgentBankingActivityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ agentBankingActivity: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AgentBankingActivityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AgentBankingActivityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load agentBankingActivity on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.agentBankingActivity).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
