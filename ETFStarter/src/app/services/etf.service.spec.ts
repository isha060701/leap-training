import { TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'

import { EtfService } from './etf.service';
import { Etf } from '../models/etf';

describe('EtfService', () => {
  let service: EtfService;
  let httpTestingController: HttpTestingController;
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

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule]
    });
    service = TestBed.inject(EtfService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return cars', inject([EtfService], fakeAsync((service: EtfService) => { 
    let etfs: Etf[] = []; 
    service.getEtfs() .subscribe(data => etfs = data); 
    const req = httpTestingController.expectOne( 'http://localhost:8080/etfs?filter=');
     // Assert that the request is a GET. 
     expect(req.request.method).toEqual('GET'); 
     // Respond with mock data, causing Observable to resolve. 
     req.flush(testEtfs); // Cause all Observables to complete and check the results 
     tick(); 
     expect(etfs[0].Ticker).toBe('BAR'); })));
});
