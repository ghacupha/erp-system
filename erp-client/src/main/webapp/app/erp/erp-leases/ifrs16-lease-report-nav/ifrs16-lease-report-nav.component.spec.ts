import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { provideMockStore, MockStore } from '@ngrx/store/testing';

import { Ifrs16LeaseReportNavComponent } from './ifrs16-lease-report-nav.component';
import { ifrs16LeaseContractReportSelected } from '../../store/actions/ifrs16-lease-contract-report.actions';

describe('Ifrs16LeaseReportNavComponent', () => {
  let component: Ifrs16LeaseReportNavComponent;
  let fixture: ComponentFixture<Ifrs16LeaseReportNavComponent>;
  let store: MockStore;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, RouterTestingModule],
      declarations: [Ifrs16LeaseReportNavComponent],
      providers: [provideMockStore()],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    store = TestBed.inject(MockStore);
    router = TestBed.inject(Router);
    spyOn(store, 'dispatch');
    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));

    fixture = TestBed.createComponent(Ifrs16LeaseReportNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should dispatch the selected lease contract and navigate to the dashboard', () => {
    const leaseContract = { id: 55 } as any;

    component.updateLeaseContract(leaseContract);
    component.launchReport();

    expect(store.dispatch).toHaveBeenCalledWith(
      ifrs16LeaseContractReportSelected({ selectedLeaseContractId: 55 })
    );
    expect(router.navigate).toHaveBeenCalledWith(['/lease-liability-schedule-view', 55]);
  });

  it('should mark the lease contract control when navigation is attempted without a selection', () => {
    component.launchReport();

    expect(component.editForm.get('leaseContract')!.touched).toBeTrue();
    expect(router.navigate).not.toHaveBeenCalled();
  });

});
