package org.openregistry.core.web;

import org.openregistry.core.domain.*;
import org.openregistry.core.domain.sor.SorPerson;
import org.openregistry.core.domain.sor.SorRole;
import org.openregistry.core.domain.sor.SorSponsor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.text.SimpleDateFormat;

import org.openregistry.core.web.propertyeditors.*;
import org.openregistry.core.service.PersonService;
import org.openregistry.core.service.ServiceExecutionResult;
import org.openregistry.core.repository.ReferenceRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author Nancy Mond
 * @version $Revision$ $Date$
 * @since 1.0.0
 */
@Controller
@RequestMapping("/addRole.htm")
@SessionAttributes({"role", "person"})
public final class RoleController {

    protected final String ACTIVE_STATUS = "Active";
    protected final String TYPE_STATUS = "Status";
    protected final String TYPE_ADDRESS = "Address";
    protected final String TYPE_PHONE = "Phone";
    protected final String TYPE_EMAIL_ADDRESS = "EmailAddress";
    protected final String CAMPUS = "Campus";
    protected final String TYPE_SPONSOR = "Sponsor";
    protected final String PERSON = "Person";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

    private String dateFormat = DEFAULT_DATE_FORMAT;

    private final SpringErrorValidationErrorConverter converter = new SpringErrorValidationErrorConverter();

    @Autowired(required=true)
    private MessageSource messageSource;


    @Autowired(required=true)
    private PersonService personService;

    @Autowired(required = true)
    private ReferenceRepository referenceRepository;

    @ModelAttribute("countryLookup")
    public List<Country> populateCountryLookup() {
        return this.referenceRepository.getCountries();
    }

    @ModelAttribute("sponsorLookup")
    public List<Person> populateSponsorLookup() {
        return this.referenceRepository.getPeople();
    }

	@RequestMapping(method = RequestMethod.GET)
    public String addRoleSetUpForm(final ModelMap model, @RequestParam("sorPersonKey") final Long sorPersonKey, @RequestParam("roleInfoKey") final Long roleInfoKey) {
    	logger.info("Populating: setUpForm: ");

        final SorPerson sorPerson = this.personService.findSorPersonById(sorPersonKey);
        final RoleInfo roleInfo = this.referenceRepository.getRoleInfo(roleInfoKey);
        final SorRole sorRole = addRole(sorPerson, roleInfo);

        model.addAttribute("role", sorRole);
        model.addAttribute("person", sorPerson);
    	return "addRole";
    }

	@RequestMapping(method = RequestMethod.POST)
	public String addRoleProcessSubmit(final ModelMap model, @ModelAttribute("person") final SorPerson sorPerson, @ModelAttribute("role") final SorRole sorRole, final BindingResult result, final SessionStatus status) {
        logger.info("processSubmit in add role");

        if (!result.hasErrors()){
            final ServiceExecutionResult serviceExecutionResult = this.personService.validateAndSaveRoleForSorPerson(sorPerson, sorRole);
            if (serviceExecutionResult.getValidationErrors().isEmpty()) {
                model.addAttribute("infoModel", this.messageSource.getMessage("roleAdded", null, null));
                status.setComplete();
            } else {
                this.converter.convertValidationErrors(serviceExecutionResult.getValidationErrors(), result);
            }
        }

        model.addAttribute("role", sorRole);
        model.addAttribute("person", sorPerson);
		return "addRole";
	}

    @InitBinder
    protected void initDataBinder(final HttpServletRequest request, final ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Date.class, createNewCustomDateEditor());
        binder.registerCustomEditor(String.class, "phones.number", new PhoneEditor());
        binder.registerCustomEditor(String.class, "phones.areaCode", new PhoneEditor());
        binder.registerCustomEditor(String.class, "phones.countryCode", new PhoneEditor());
        binder.registerCustomEditor(Country.class, new CountryEditor(referenceRepository));
        binder.registerCustomEditor(Region.class, new RegionEditor(referenceRepository));
        binder.registerCustomEditor(Person.class, new SponsorEditor(referenceRepository));
    }

    /**
     * Add and initialize new role.
     * @param sorPerson
     * @param roleInfo
     * @return sorRole
     */
    protected SorRole addRole(final SorPerson sorPerson, final RoleInfo roleInfo){
        final SorRole sorRole = sorPerson.addRole(roleInfo);
        sorRole.setSorId("1");  // TODO Don't hardcode
        sorRole.setSourceSorIdentifier("or-webapp"); // TODO Don't hardcode
        sorRole.setPersonStatus(referenceRepository.findType(TYPE_STATUS, ACTIVE_STATUS));
        final EmailAddress emailAddress = sorRole.addEmailAddress();
        emailAddress.setAddressType(referenceRepository.findType(TYPE_EMAIL_ADDRESS, CAMPUS));
        final Phone phone = sorRole.addPhone();
        phone.setPhoneType(referenceRepository.findType(TYPE_PHONE, CAMPUS));
        phone.setAddressType(referenceRepository.findType(TYPE_PHONE, CAMPUS));
        final Address address = sorRole.addAddress();
        address.setType(referenceRepository.findType(TYPE_ADDRESS, CAMPUS));
        final SorSponsor sponsor = sorRole.setSponsor();
        sponsor.setType(referenceRepository.findType(TYPE_SPONSOR, PERSON));  // TODO handle other types

        //provide default values for start and end date of role
        final Calendar cal = Calendar.getInstance();
        sorRole.setStart(cal.getTime());
        cal.add(Calendar.MONTH, 6);
        sorRole.setEnd(cal.getTime());
        return sorRole;
    }

    public final void setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
    }

    protected final CustomDateEditor createNewCustomDateEditor() {
        return new CustomDateEditor(new SimpleDateFormat(this.dateFormat), true);
    }
}