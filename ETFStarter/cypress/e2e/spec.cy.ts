import { AppPageObject } from "cypress/support/app.po";
import { TablePageObject } from "cypress/support/etf.po";
let appPage: AppPageObject = new AppPageObject();
let tablePage: TablePageObject = new TablePageObject();

describe("My First Test", () => {
  it("Visits the initial project page", () => {
    appPage.navigateToHomePage();
    appPage.checkTitle("Welcome to ETF");
  });

});

describe("My Second Test", () => {
  it("contains headers and a table cars", () => {
    appPage.navigateToHomePage();
    appPage.checkTitle("Welcome to ETF");
    tablePage.checkBookTableHeader("ETFs");
    tablePage.getBookTableRows().should("have.length.gte", 2);
    tablePage.getBookTableFirstRowTitleColumn().should("eq", 'BAR');
    tablePage.getBookTableLastRowTitleColumn().should("eq", "USO");
  });
});

