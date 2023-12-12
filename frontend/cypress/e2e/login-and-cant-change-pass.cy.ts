it('Login and cant change pass', () => {
  cy.visit('/login');
  cy.url().should('include', '/login');
  cy.get('.form.nickname input').clear().type('admin');
  cy.get('.form.password input').clear().type('admincool');
  cy.get('.content .enter').click();
  cy.url().should('not.include', 'login');
  cy.url().should('include', '/');
  cy.get('header .bottom .container .actions .button-icon:nth-child(3)').should((profileButton) => {
    expect(profileButton, 'Profile button existing').to.exist;
  });
  cy.get('header .bottom .container .actions .button-icon:nth-child(3)').click();
  cy.get('header .bottom .container .actions .button-icon:nth-child(3) poly-menu .menu .footer .item:nth-child(1)').should("exist");
  cy.get('header .bottom .container .actions .button-icon:nth-child(3) poly-menu .menu .footer .item:nth-child(1)').click();
  cy.url().should('include', '/profile-settings');
  cy.get('.form.old-password input').clear().type('admincool1');
  cy.get('.form.new-password input').clear().type('admincool1');
  cy.get('.form.confirm-password input').clear().type('admincool1');
  cy.get('.content > button').click();
  cy.url().should('include', 'profile-settings');
});
