import { TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'
import { ShopService } from './shop.service';
import { Shop } from '../models/shop';

describe('ShopService', () => {
  let service: ShopService;
  let httpTestingController: HttpTestingController;
  const testShops:Shop[]=[
    {
      "cost": 1600,
      "description": "Useful for Business purpose",
      "id": 1,
      "name": "15 inch Laptop"
      },
      {
      "cost": 110,
      "description": "Crisp, balanced sound and unmatched bass",
      "id": 2,
      "name": "Bose Micro Bluetooth Speaker"
      }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule]
    });
    service = TestBed.inject(ShopService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return cars', inject([ShopService], fakeAsync((service: ShopService) => { 
    let shops: Shop[] = []; 
    service.getShops() .subscribe(data => shops = data); 
    const req = httpTestingController.expectOne('http://localhost:8080/ShoppingCartService/jaxrs/products');
     // Assert that the request is a GET. 
     expect(req.request.method).toEqual('GET'); 
     // Respond with mock data, causing Observable to resolve. 
     req.flush(testShops); // Cause all Observables to complete and check the results 
     tick(); 
     expect(shops[0].cost).toBe(1600); })));
});
