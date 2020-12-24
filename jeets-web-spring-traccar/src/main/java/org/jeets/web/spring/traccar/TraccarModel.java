package org.jeets.web.spring.traccar;

import java.util.List;
import javax.annotation.PostConstruct;
import org.jeets.web.spring.annotations.IsTraccarAdminOrManager;
import org.jeets.web.spring.annotations.IsTraccarUser;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.Device;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Provides the original Traccar Model as a starting point for rapid development of Controllers and
 * Views.
 *
 * <p>Technically a wrapper for each Traccar ORM and Cache for server responses.
 *
 * <p>Internally it will hold the original Traccar Model and keep it in sync via Traccar API. It
 * also serves as a cache for previously retrieved model details in order to save redundant
 * requests. (add .refresh method/s) The instance is created via Authentication and lives through a
 * session life cycle.
 */
// big TODO: integrate Traccar ERM in OpenAPI client and rewrite with ORM navigation
@Service
@SessionScope
public class TraccarModel {

  // apply SpringMVC concepts: Model, View, ModelAndView, ModelAttributes etc. ?

  /**
   * Traccar API can not be accessed externally and was created with an authenticated API, an
   * authorized User and an initiated session. During the session the API is accessed analog to a
   * repository.
   */
  private DefaultApi traccarApi;

  /**
   * This authentication only works after the TraccarAuthenticationProvider has authenticated a user
   * against the Traccar Server and who is authorized for Spring Security. As a consequence: <br>
   * DO NOT INJECT THE TraccarModel <br>
   * BEFORE THE TraccarAuthenticationProvider HAS DONE HIS JOB!
   *
   * <p>If this method ever fails it might make sense to call this (public!) method at a later point
   * again?
   */
  @PostConstruct
  private void authenticate() {
    if (TraccarAuthentication.getAuthentication().isAuthenticated()) {
      // The Traccar API must be created at login time and must have been
      // attached to the Authentication before it can be retrieved here.
      traccarApi = (DefaultApi) TraccarAuthentication.getAuthentication().getDetails();
      // catch and handle CastExep ?
      System.out.println(
          "authenticated Traccar User " + TraccarAuthentication.getTraccarUser().getName());
    }
    // else is NOT authenticated ..

    // error handling ?

    initializeOrm();
  }

  /** return one readable role string for frontend. */
  // TODO move strings out of Model and add i18n externally
  public String getRoleString() {
    if (TraccarAuthentication.isAdmin()) {
      return "Administrator";
    } else if (TraccarAuthentication.isManager()) {
      return "Manager";
    } else if (TraccarAuthentication.isUser()) {
      return "User";
    } else {
      return null; // add guestUser
    }
  }

  /** Initialize all values to avoid null and NPEx on initial views. */
  // public void    refreshOrm() { to explicitly access server
  // public void      printOrm() { for dashboard
  public void initializeOrm() { // populateModel(Model m)
    System.out.println("initializeOrm ..");
    requestUserDevices(); // works for any user role
    // user/s, group/s ...
  }

  // ==================== The Static Traccar Model ====================

  // part of the Spring MVC Model
  // each device brings its own ORM to be filled
  // TODO display ${devices} in a html view as reference to @ModelAttribute
  /**
   * The Device list is created for session use and can be used to lookup each Device (by different
   * search methods).
   *
   * <p>The Device itself represents the entry point to the ORM. For example you lookup Events via
   * Device.getEvents(filtered).
   */
  private List<Device> devices;

  /**
   * Fetch device list from view class.
   *
   * <p>Note the device list has to be set by a set*Devices() method.
   */
  public List<Device> getDevices() {
    return devices;
  }

  // ==================================================================

  /*
   * on a longer term the three (four) access categories should be distributed
   * over inner classes: Admin, Manager, AdminOrManager, User
   * 		TraccarModel.Admin.getDevices
   * 		TraccarModel.User .getDevices
   * 		TraccarModel.getDevices - result in model
   * for clearer structures and hierarchy at dev time
   * and Security Annotations at Class Level !
   */

  /** Retrieves all devices connected to the current user - in any role. */
  @IsTraccarUser
  // @ModelAttribute("devices") // (binding=false)
  public void requestUserDevices() {
    // only retrieve ALL devices once per session (?)
    // if (devices != null && !devices.isEmpty() && refresh())
    // return devices;
    // model.addAttribute("test", new Integer(12));
    System.out.println("setUserDevices");
    try {
      /*
         *      ALL - Can only be used by admins or managers to fetch all entities (optional)
      => *   userId - Standard users can use this only with their own _userId_ (optional)
         *       id - To fetch one or more devices.
         *            Multiple params can be passed like `id=31&id=42` (optional)
         * uniqueId - To fetch one or more devices.
         *      Multiple params can be passed like `uniqueId=333331&uniqieId=44442` (optional)
         */
      devices =
          traccarApi.devicesGet(null, TraccarAuthentication.getTraccarUser().getId(), null, null);
      // return
      // traccarApi.devicesGet(null,TraccarAuthentication.getTraccarUser().getId(),null,null);
      //  return traccarApi.devicesGet(true,null,null,null); // admin only !!

    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      // e.printStackTrace();
    }
  }

  /** */
  @IsTraccarAdminOrManager
  // HOW and WHERE TO CATCH ??
  // 2020-11-27 18:26:53.341 ERROR 10500 --- [nio-8080-exec-7]
  // com.VAADIN.flow.server.DefaultErrorHandler
  // org.springframework.security.access.AccessDeniedException: Zugriff verweigert
  // TODO apply @PreAuthorize instead of @PostAuthorize (at dev time to println API Exception)
  public void requestAllDevices() {
    // only retrieve ALL devices once per session (?)
    // if (devices != null && !devices.isEmpty())
    // return devices;
    System.out.println("setAllDevices");
    try {
      // ALL - Can only be used by AdminOrManagers to fetch all entities (optional)
      devices = traccarApi.devicesGet(true, null, null, null);

    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      // Manager access required - SecurityException (PermissionsManager:178)
      System.err.println("Response headers: " + e.getResponseHeaders());
      // e.printStackTrace();
      // in order to supply developer infos at dev time (?)
      // throw new UnauthorizedException();
    }
  }

  // TODO
  @ExceptionHandler(AccessDeniedException.class)
  public void handleAccessDeniedException(Exception ex) {
    System.err.println("handle AccessDeniedException ...");
  }
}
