export class Etf {
  constructor(
    public Ticker: string,
    public Fund_Name: string,
    public Issuer: string,
    public AUM_bil: number,
    public Expense_Ratio: number,
    public ThreeMoTR: number,
    public Segment: string
  ) {}
}
