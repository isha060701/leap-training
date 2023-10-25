import { Injectable } from '@angular/core';
import { Observable, catchError, of, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Shop } from '../models/shop';

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  constructor(private http:HttpClient) { }

  private url="http://localhost:8080/ShoppingCartService/jaxrs/products";

  shops:Shop[]=[
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

  getShops(): Observable<Shop[]>{
    return this.http.get<Shop[]>(this.url);
    // return of(this.cars)
  }

  addShop():Observable<any>{
    return this.http.get<any>('http://localhost:8080/ShoppingCartService/jaxrs/cart?price=1000');
  }

}
