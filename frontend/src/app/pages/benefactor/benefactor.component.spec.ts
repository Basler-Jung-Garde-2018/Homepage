import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BenefactorComponent } from './benefactor.component';

describe('BenefactorComponent', () => {
  let component: BenefactorComponent;
  let fixture: ComponentFixture<BenefactorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BenefactorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BenefactorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
