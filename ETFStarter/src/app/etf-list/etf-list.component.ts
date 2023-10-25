import { Component } from '@angular/core';
import { Etf } from '../models/etf';
import { EtfService } from '../services/etf.service';

@Component({
  selector: 'app-etf-list',
  templateUrl: './etf-list.component.html',
  styleUrls: ['./etf-list.component.css']
})
export class EtfListComponent {
  errorMessage: string="";

  constructor(private etfService:EtfService){}

  etfs:Etf[]=[
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

  ngOnInit()
  {
    this.loadAllEtfs();
  } 


  loadAllEtfs()
  {
    this.etfService.getEtfs()
    .subscribe({ next : (data) => { 
      this.etfs = data; 
      this.errorMessage = '';}, error: (e) => this.errorMessage = e 
    });
  }

  loadEtfsByFilter(filter:string){
    this.etfService.getEtfsWithFilters(filter).subscribe(data=>{
      this.etfs=data;
    })}
}
