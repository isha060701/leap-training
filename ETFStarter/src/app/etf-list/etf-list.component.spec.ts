import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EtfListComponent } from './etf-list.component';
import { Etf } from '../models/etf';
import { of } from 'rxjs';
import { EtfService } from '../services/etf.service';

describe('EtfListComponent', () => {
  let component: EtfListComponent;
  let fixture: ComponentFixture<EtfListComponent>;
  const testEtfs:Etf[]=[
    {
      "Ticker": "BAR",
      "Fund_Name": "GraniteShares Gold Trust",
      "Issuer": "GraniteShares",
      "AUM_bil": 0.877,
      "Expense_Ratio": 0.0017,
      "ThreeMoTR": 0.0795,
      "Segment": "Commodities: Precious Metals Gold"
      },
      {
      "Ticker": "DBC",
      "Fund_Name": "Invesco DB Commodity Index Tracking Fund",
      "Issuer": "Invesco",
      "AUM_bil": 0.769,
      "Expense_Ratio": 0.0089,
      "ThreeMoTR": -0.2505,
      "Segment": "Commodities: Broad Market"
      }
  ]
  let mockEtfService: any = jasmine.createSpyObj('EtfService', ['getEtfs']);
  mockEtfService.getEtfs.and.returnValue(of(testEtfs));

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EtfListComponent ],
      providers: [{ provide: EtfService, useValue: mockEtfService }],
    })
    .compileComponents();

    fixture = TestBed.createComponent(EtfListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should retrieve cars from the service', () => {
    expect(component.etfs.length).toBeGreaterThanOrEqual(2);
    expect(component.etfs[0].Ticker).toBe('BAR');
    expect(component.etfs[1].Ticker).toBe('DBC');
  });

  it('should contain a table', () => {
    component.etfs = [
      {
        "Ticker": "BAR",
        "Fund_Name": "GraniteShares Gold Trust",
        "Issuer": "GraniteShares",
        "AUM_bil": 0.877,
        "Expense_Ratio": 0.0017,
        "ThreeMoTR": 0.0795,
        "Segment": "Commodities: Precious Metals Gold"
        },
        {
        "Ticker": "DBC",
        "Fund_Name": "Invesco DB Commodity Index Tracking Fund",
        "Issuer": "Invesco",
        "AUM_bil": 0.769,
        "Expense_Ratio": 0.0089,
        "ThreeMoTR": -0.2505,
        "Segment": "Commodities: Broad Market"
        }
    ];
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    const table = compiled.querySelector('table');
    console.log(table);
    expect(table.rows.length).toBeGreaterThanOrEqual(3);
    expect(table.rows[1].cells[0].textContent).toBe('BAR');
  });
});
