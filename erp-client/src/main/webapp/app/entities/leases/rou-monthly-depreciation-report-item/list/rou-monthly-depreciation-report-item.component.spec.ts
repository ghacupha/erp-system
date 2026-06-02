jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouMonthlyDepreciationReportItemService } from '../service/rou-monthly-depreciation-report-item.service';

import { RouMonthlyDepreciationReportItemComponent } from './rou-monthly-depreciation-report-item.component';

describe('RouMonthlyDepreciationReportItem Management Component', () => {
  let comp: RouMonthlyDepreciationReportItemComponent;
  let fixture: ComponentFixture<RouMonthlyDepreciationReportItemComponent>;
  let service: RouMonthlyDepreciationReportItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RouMonthlyDepreciationReportItemComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { queryParams: {} } },
        },
      ],
    })
      .overrideTemplate(RouMonthlyDepreciationReportItemComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouMonthlyDepreciationReportItemComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RouMonthlyDepreciationReportItemService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.rouMonthlyDepreciationReportItems[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  it('should load a page', () => {
    // WHEN
    comp.loadPage(1);

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.rouMonthlyDepreciationReportItems[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  it('should calculate the sort attribute for an id', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalledWith(expect.objectContaining({ sort: ['id,asc'] }));
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    // INIT
    comp.ngOnInit();

    // GIVEN
    comp.predicate = 'name';

    // WHEN
    comp.loadPage(1);

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['name,asc', 'id'] }));
  });

  it('should re-initialize the page', () => {
    // WHEN
    comp.loadPage(1);
    comp.reset();

    // THEN
    expect(comp.page).toEqual(0);
    expect(service.query).toHaveBeenCalledTimes(2);
    expect(comp.rouMonthlyDepreciationReportItems[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
