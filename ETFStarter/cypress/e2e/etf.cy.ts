import { AppPageObject } from "cypress/support/app.po"

let app:AppPageObject=new AppPageObject()
describe('car list page tests', () => {
  beforeEach(()=>{
    app.navigateToHomePage()
  })
    
  it('check if the load all etfs button works', () => {
    cy.get('#getAllEtfs').click()
    cy.get('table').should('be.visible');
  })
  it('check if the load all etfs by Three button works', () => {
    cy.get('#getByThree').click()
    cy.get('table').should('be.visible');
  })
  it('check if the load all etfs by Expense button works', () => {
    cy.get('#getByExpense').click()
    cy.get('table').should('be.visible');
  })
  it('check if the load all etfs by Aum button works', () => {
    cy.get('#getByAum').click()
    cy.get('table').should('be.visible');
  })
})
