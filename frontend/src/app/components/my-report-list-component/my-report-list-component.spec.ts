import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyReportListComponent } from './my-report-list-component';

describe('MyReportListComponent', () => {
  let component: MyReportListComponent;
  let fixture: ComponentFixture<MyReportListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyReportListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyReportListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
