import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopListComponent } from './shop-list.component';
import { Shop } from '../models/shop';
import { of } from 'rxjs';
import { ShopService } from '../services/shop.service';

describe('ShopListComponent', () => {
  let component: ShopListComponent;
  let fixture: ComponentFixture<ShopListComponent>;

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

  let mockShopService: any = jasmine.createSpyObj('ShopService', ['getShops']);
  mockShopService.getShops.and.returnValue(of(testShops));
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShopListComponent ],
      providers: [{ provide: ShopService, useValue: mockShopService }],
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShopListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should retrieve cars from the service', () => {
    expect(component.shops.length).toBeGreaterThanOrEqual(2);
    expect(component.shops[0].cost).toBe(1600);
    expect(component.shops[1].cost).toBe(110);
  }); 

  it('should contain a table', () => {
    component.shops = [
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
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    const table = compiled.querySelector('table');
    console.log(table);
    expect(table.rows.length).toBeGreaterThanOrEqual(3);
    expect(table.rows[1].cells[1].textContent).toBe('Useful for Business purpose');
  });
});
