import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicCalenderComponent } from './public-calender.component';

describe('CalendarComponent', () => {
  let component: PublicCalenderComponent;
  let fixture: ComponentFixture<PublicCalenderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicCalenderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublicCalenderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
