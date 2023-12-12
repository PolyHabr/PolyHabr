it('Add comment article', () => {
  cy.visit('/login');
  cy.url().should('include', '/login');
  cy.get('.form.nickname input').clear().type('admin');
  cy.get('.form.password input').clear().type('admincool');
  cy.get('.content .enter').click();
  cy.url().should('not.include', 'login');
  cy.url().should('include', '/');
  cy.get('poly-card:nth-child(2) article > a').click();
  let comment = (Math.random() + 1).toString(36).substring(7);
  cy.get('.comment-input textarea').clear().type(comment);
  cy.get('.comment-input button').click();
  cy.get('poly-comment:first-child .text').should("have.text", comment);
});
