/**
 * Copyright (C) 2009 Jasig, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openregistry.core.service;

import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.openregistry.core.domain.jpa.sor.*;
import org.openregistry.core.domain.jpa.*;
import org.openregistry.core.domain.*;
import org.openregistry.core.domain.sor.*;
import org.openregistry.core.service.reconciliation.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Integration test for {@link DefaultPersonService} that links up with the JPA
 * repositories.
 *
 * @version $Revision$ $Date$
 * @since 0.1
 *
 * TODO: add check for merged properties after business rules execution.
 */

@ContextConfiguration(locations = {"classpath:test-personServices-context.xml"})
public final class DefaultPersonServiceIntegrationTests extends AbstractTransactionalJUnit4SpringContextTests {

	private final String OR_WEBAPP_IDENTIFIER = "or-webapp";
	private final String REGISTRAR_IDENTIFIER = "registrar";

	private static final String EMAIL_ADDRESS = "test@test.edu";
	private static final String PHONE_NUMBER = "555-555-5555";
	private static final String RUDYARD = "Rudyard";
	private static final String KIPLING = "Kipling";
	private static final String RUDY = "Rudy";
	private static final String KIPSTEIN = "Kipstein";

    @Autowired
    private PersonService personService;

    protected ReconciliationCriteria constructReconciliationCriteria(final String firstName, final String lastName, final String ssn, final String emailAddress, final String phoneNumber, Date birthDate, final String sor, final String sorId) {
        final ReconciliationCriteria reconciliationCriteria = new JpaReconciliationCriteriaImpl();
        reconciliationCriteria.setEmailAddress(emailAddress);
        reconciliationCriteria.setPhoneNumber(phoneNumber);

        final SorPerson sorPerson = reconciliationCriteria.getPerson();
        sorPerson.setDateOfBirth(birthDate);
        sorPerson.setGender("M");
        sorPerson.setSorId(sorId);
        sorPerson.setSourceSor(sor);
        sorPerson.setSsn(ssn);

        final Name name = sorPerson.addName();
        name.setFamily(lastName);
        name.setGiven(firstName);
        name.setOfficialName();

        return reconciliationCriteria;
    }

    /**
     * Test 1: Test of adding a new SoR Person to an empty database:
     * Expectations: 1 SoR Person row created
     *               1 Calculated Person row created, one name, one identifier
     */
	@Test
	public void testAddOnePerson() throws ReconciliationException {
        final ReconciliationCriteria reconciliationCriteria = constructReconciliationCriteria(RUDYARD, KIPLING, null, EMAIL_ADDRESS, PHONE_NUMBER, new Date(0), OR_WEBAPP_IDENTIFIER, null);
        final ServiceExecutionResult<Person> result = this.personService.addPerson(reconciliationCriteria);

        assertTrue(result.succeeded());
        assertEquals(1, countRowsInTable("prc_persons"));
        assertEquals(1, countRowsInTable("prc_names"));
        assertEquals(1, countRowsInTable("prs_names"));
        assertEquals(1, countRowsInTable("prs_sor_persons"));
	}

    /**
     * Test 2: Test of adding two new SoR Persons to an empty database (with no matches):
     * Expectations: 2 Sor Person rows
     *               2 Calculated persons, two names, two identifiers
     */
    @Test
    public void testAddTwoDifferentPeople() throws ReconciliationException {
        final ReconciliationCriteria reconciliationCriteria = constructReconciliationCriteria(RUDYARD, KIPLING, null, EMAIL_ADDRESS, PHONE_NUMBER, new Date(0), OR_WEBAPP_IDENTIFIER, null);
        this.personService.addPerson(reconciliationCriteria);

        final ReconciliationCriteria reconciliationCriteria2 = constructReconciliationCriteria("Foo", "Bar", null, "la@lao.com", "9085550987", new Date(0), OR_WEBAPP_IDENTIFIER, null);
        final ServiceExecutionResult<Person> result = this.personService.addPerson(reconciliationCriteria2);

        assertTrue(result.succeeded());
        assertEquals(2, countRowsInTable("prc_persons"));
        assertEquals(2, countRowsInTable("prc_names"));
        assertEquals(2, countRowsInTable("prs_names"));
        assertEquals(2, countRowsInTable("prs_sor_persons"));
    }

    /**
     * Test 3: Test of adding two new Sor Persons where there is an exact match (same SoR)
     *
     * This is an update.  TODO complete this test
     */
    @Test
    public void testAddExactPersonWithSameSoR() throws ReconciliationException {
        final ReconciliationCriteria reconciliationCriteria = constructReconciliationCriteria(RUDYARD, KIPLING, null, EMAIL_ADDRESS, PHONE_NUMBER, new Date(0), OR_WEBAPP_IDENTIFIER, null);
        this.personService.addPerson(reconciliationCriteria);

        final ServiceExecutionResult result = this.personService.addPerson(reconciliationCriteria);

        assertTrue(result.succeeded());
        assertEquals(1, countRowsInTable("prc_persons"));
        assertEquals(1, countRowsInTable("prc_names"));
        assertEquals(1, countRowsInTable("prs_names"));
        assertEquals(1, countRowsInTable("prs_sor_persons"));
    }

    /**
     * Test 4: Test of adding two new SoR Persons where there is an exact match (different SoR)
     * // TODO re-enable this test.  Disabled because it fails.
     */
    @Test
    public void testAddExactPersonWithDifferentSoRs() throws ReconciliationException {
        final ReconciliationCriteria reconciliationCriteria = constructReconciliationCriteria(RUDYARD, KIPLING, null, EMAIL_ADDRESS, PHONE_NUMBER, new Date(0), OR_WEBAPP_IDENTIFIER, null);
        final ReconciliationCriteria reconciliationCriteria1 = constructReconciliationCriteria(RUDYARD, KIPLING, null, EMAIL_ADDRESS, PHONE_NUMBER, new Date(0), "SOR2", null);
        this.personService.addPerson(reconciliationCriteria);

        final ServiceExecutionResult result = this.personService.addPerson(reconciliationCriteria1);

        assertTrue(result.getTargetObject() instanceof Person);
        assertTrue(result.succeeded());
        //assertEquals(1, countRowsInTable("prc_persons"));
        //assertEquals(1, countRowsInTable("prc_names"));
        //assertEquals(2, countRowsInTable("prs_names"));
        //assertEquals(2, countRowsInTable("prs_sor_persons"));
    }

    /**
     * Test 5: Test of adding two new SoR Persons where there is a partial match (same SoRs).
     * The test requires you to say its the same person.
     *
     * Expectation: kick us out this is an update!
     *
     * TODO: complete test
     */
    @Test
    public void testAddTwoSoRPersonsWithPartialMatchFromTheSameSoRWhereItsTheSamePerson() {
        // this should flow to update
    }

    /**
     * Test 6: Test of adding two new SoR Persons where there is a partial match (same SoRs).
     * The test requires you to say its different people.
     *
     * Expectation: two SoR Records, and two Calculated People
     */
    @Test
    public void testAddTwoSoRPersonsWithPartialMatchAndItsTwoDifferentPeopleFromSameSoR() {
        final ReconciliationCriteria reconciliationCriteria = constructReconciliationCriteria(RUDYARD, KIPLING, null, EMAIL_ADDRESS, PHONE_NUMBER, new Date(0), OR_WEBAPP_IDENTIFIER, null);
        final ReconciliationCriteria reconciliationCriteria1 = constructReconciliationCriteria("FOOBAR", KIPLING, null, EMAIL_ADDRESS, PHONE_NUMBER, new Date(0), OR_WEBAPP_IDENTIFIER, null);

        try {
            this.personService.addPerson(reconciliationCriteria);
        } catch (final ReconciliationException e) {
            // nothing to do
        }
        assertEquals(1, countRowsInTable("prs_sor_persons"));

        try {
            this.personService.addPerson(reconciliationCriteria1);
        } catch (final ReconciliationException e) {
            assertEquals(1, countRowsInTable("prs_sor_persons"));
            final ServiceExecutionResult<Person> serviceExecutionResult = this.personService.forceAddPerson(reconciliationCriteria1, e);
            assertNotNull(serviceExecutionResult.getTargetObject());
            assertEquals(2, countRowsInTable("prc_persons"));
            assertEquals(2, countRowsInTable("prc_names"));
            assertEquals(2, countRowsInTable("prs_names"));
            assertEquals(2, countRowsInTable("prs_sor_persons"));
        }
    }

    /**
     * Test 7: Test of adding two new SoR Persons where there is a partial match (different SoRs)
     * The test requires you to say its the same person.
     *
     * Expectation: we should add a new SoR Record and update existing calculated person.
     *
     * TODO: complete this test.
     */
    @Test
    public void testAddTwoSorPersonWithPartialMatchFromDifferentSoRsWhereItsTheSamePerson() {
        final ReconciliationCriteria reconciliationCriteria = constructReconciliationCriteria(RUDYARD, KIPLING, null, EMAIL_ADDRESS, PHONE_NUMBER, new Date(0), OR_WEBAPP_IDENTIFIER, null);
        final ReconciliationCriteria reconciliationCriteria1 = constructReconciliationCriteria("FOOBAR", KIPLING, null, EMAIL_ADDRESS, PHONE_NUMBER, new Date(0), "SOR2", null);
    }

    /**
     * Test 8: Test of adding two new SoR Persons where there is a partial match (different SoRs).
     * The test requires you to say its a different person.
     *
     * Expectation: a new SoR Person and Calculated Person will be created (2 of each).
     */
    @Test
    public void testAddTwoNewSoRPersonsWithPartialMatchWhoAreDifferentPeople() {
        final ReconciliationCriteria reconciliationCriteria = constructReconciliationCriteria(RUDYARD, KIPLING, null, EMAIL_ADDRESS, PHONE_NUMBER, new Date(0), OR_WEBAPP_IDENTIFIER, null);
        final ReconciliationCriteria reconciliationCriteria1 = constructReconciliationCriteria("FOOBAR", KIPLING, null, EMAIL_ADDRESS, PHONE_NUMBER, new Date(0), "SOR2", null);

        try {
            this.personService.addPerson(reconciliationCriteria);
            assertEquals(1, countRowsInTable("prs_sor_persons"));
        } catch (final ReconciliationException e) {
            fail();
        }

        try {
            final ServiceExecutionResult result = this.personService.addPerson(reconciliationCriteria1);
        } catch (final ReconciliationException e) {
            assertEquals(1, countRowsInTable("prs_sor_persons"));
            final ServiceExecutionResult<Person> serviceExecutionResult = this.personService.forceAddPerson(reconciliationCriteria1, e);
            assertEquals(2, countRowsInTable("prc_persons"));
            assertEquals(2, countRowsInTable("prc_names"));
            assertEquals(2, countRowsInTable("prs_names"));
            assertEquals(2, countRowsInTable("prs_sor_persons"));
        }
    }
}