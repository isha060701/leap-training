import { Component } from '@angular/core';
import { Shop } from '../models/shop';
import { ShopService } from '../services/shop.service';

@Component({
  selector: 'app-shop-list',
  templateUrl: './shop-list.component.html',
  styleUrls: ['./shop-list.component.css']
})
export class ShopListComponent {
  errorMessage: string | undefined;

  constructor(private shopService:ShopService){}
  
  shopCount:any;
  shops:Shop[]=[
    // {
    //   "cost": 1600,
    //   "description": "Useful for Business purpose",
    //   "id": 1,
    //   "name": "15 inch Laptop"
    //   },
    //   {
    //   "cost": 110,
    //   "description": "Crisp, balanced sound and unmatched bass",
    //   "id": 2,
    //   "name": "Bose Micro Bluetooth Speaker"
    //   }
  ];

  ngOnInit()
  {
    this.loadAllShops();
  }

  loadAllShops()
  {
    this.shopService.getShops()
    .subscribe({ next : (data) => { 
      this.shops = data; 
      this.errorMessage = '';}, error: (e) => this.errorMessage = e });
  }

  addShop()
  {
    this.shopService.addShop().subscribe({ next : (data) => { 
      this.shopCount = data; 
      this.errorMessage = '';}, error: (e) => this.errorMessage = e })
  }
}
