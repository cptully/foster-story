package com.fosterstory.controller;

import com.fosterstory.bean.Login;
import com.fosterstory.bean.Search;
import com.fosterstory.entity.Animal;
import com.fosterstory.entity.Breed;
import com.fosterstory.entity.User;
import com.fosterstory.service.FSService;
import com.fosterstory.utility.PasswordStorage;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by chris on 10/3/16.
 */
@Controller
public class FSController {

    private final Integer ADMIN = 1;
    private final Integer USER =2;
    
    @Autowired
    FSService fsService;

    // TODO: 10/5/16 search is broken
    @RequestMapping (path = "/")
    public String list(Model model,
                       Search search,
                       @PageableDefault(size = 9) Pageable pageable,
                       String action) {
        if ((action != null) && (action.equals("clear"))) {
            search = new Search();
        }

        Page<Animal> animals = fsService.listAnimals(search, pageable);
        model.addAttribute("types", fsService.listTypes());
        model.addAttribute("breeds", fsService.listBreeds());
        model.addAttribute("search", search);
        model.addAttribute("pageable", pageable);
        model.addAttribute("animals", animals);
        return "/list";
    }

    @RequestMapping (path = "/register", method = RequestMethod.GET)
    public String register(User user,
                           Model model,
                           HttpSession session) {
        model.addAttribute("user", user);
        return "/register";
    }

    @RequestMapping (path = "/register", method = RequestMethod.POST)
    public String register(@Valid User user,
                           BindingResult bindingResult,
                           @RequestParam(defaultValue = "") String confirmPassowrd,
                           Model model,
                           HttpSession session) {
        if (!bindingResult.hasErrors()) {
            try {
                if (user.getPassword().equals(confirmPassowrd)) {
                    fsService.saveUser(user);
                    session.setAttribute("userId", user.getId());
                    return "redirect:/";
                } else {
                    FieldError fieldError = new FieldError("user", "password", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[])null, "Passwords do not match");
                    bindingResult.addError(fieldError);
                }
            } catch (PasswordStorage.CannotPerformOperationException e) {
                FieldError fieldError = new FieldError("user", "password", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[])null, "Invalid password");
                bindingResult.addError(fieldError);
                e.printStackTrace();
            }
        }
             model.addAttribute("user", user);
            model.addAttribute("bindingResult", bindingResult);

        return "/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Login login, Model model, HttpSession session){

        model.addAttribute("login", login);

        // get the user (or null if not logged in)
        model.addAttribute("user", fsService.getUserOrNull((Integer)session.getAttribute("userId")));

        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model, @Valid Login login, HttpSession session){

        User user = fsService.authenticateUser(login);

        // get the user (or null if not logged in)
        model.addAttribute("user", user);

        if(user != null){
            session.setAttribute("userId", user.getId());
            return "redirect:/";
        } else {
            return "login";
        }
    }

    @RequestMapping(path = "/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/users")
    public String users(Model model, HttpSession session){
        if(session.getAttribute("userId") == null){
            return "redirect:/login";
        } else if (fsService.getUser((Integer)session.getAttribute("userId")).getRole().getId().equals(USER)){
            // user must have admin rights for this page
            model.addAttribute("users", fsService.listUsers((Integer)session.getAttribute("userId")));
        } else if (fsService.getUser((Integer)session.getAttribute("userId")).getRole().getId().equals(ADMIN)){
            // admin user can edit all users
            model.addAttribute("users", fsService.listUsers());
        }

        model.addAttribute("user", fsService.getUserOrNull((Integer)session.getAttribute("userId")));

        return "users";
    }


    // TODO: 10/1/16 fix rights logic on this and following methods
    @RequestMapping(path = "/editUser", method = RequestMethod.GET)
    public String userForm(Integer id, Model model, HttpSession session){

        if(session.getAttribute("userId") == null) {
            return "redirect:/login";
        } else if ((fsService.getUser((Integer)session.getAttribute("userId")).getRole().getId().equals(USER)) &&
                (session.getAttribute("userId") == id)) {
            // non-admin user can edit own profile
            model.addAttribute("user", fsService.getUserOrNull(id));
            return "registration";
        } else if (fsService.getUser((Integer)session.getAttribute("userId")).getRole().getId().equals(ADMIN)){
            // user must have admin rights to edit other people's profiles
            model.addAttribute("user", fsService.getUserOrNull(id));
            return "registration";
        }

        return "redirect:/";
    }

    @RequestMapping(path = "/editUser", method = RequestMethod.POST)
    public String editUser(@Valid @ModelAttribute(name = "user") User user,
                           @RequestParam(defaultValue = "") String oldPassword,
                           @RequestParam(defaultValue = "") String confirmPassword,
                           BindingResult bindingResult,
                           Model model,
                           HttpSession session){

        Integer sessionUserRole = fsService.getUser((Integer)session.getAttribute("userId")).getRole().getId();
        if(session.getAttribute("userId") == null) {
            return "redirect:/login";
        } else if (((sessionUserRole.equals(USER)) && (session.getAttribute("userId") == user.getId()))  // user can edit own profile
                || (sessionUserRole.equals(ADMIN))) {  // only admin can edit other profiles

            if (!bindingResult.hasErrors()) {

                try {
                    if (user.getId() != null) {
                        String savedPassword = fsService.getUser(user.getId()).getPassword();
                        if (PasswordStorage.verifyPassword(oldPassword, savedPassword)) {
                            if (!PasswordStorage.verifyPassword(user.getPassword(), savedPassword)) {
                                if (user.getPassword().equals(confirmPassword)) {
                                    fsService.saveUser(user);
                                    return "redirect:/users";
                                } else {
                                    // set errors
                                    FieldError fieldError = new FieldError("user", "password-match", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[]) null, "Passwords do not match");
                                    bindingResult.addError(fieldError);
                                }
                            } else {
                                // new matches old
                                FieldError fieldError = new FieldError("user", "password-old-new", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[]) null, "New password cannot match old password");
                                bindingResult.addError(fieldError);
                            }
                        } else {
                            // invalid password
                            FieldError fieldError = new FieldError("user", "badpassword", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[]) null, "Username / password combination incorrect");
                            bindingResult.addError(fieldError);
                        }
                    } else {
                        fsService.saveUser(user);
                        return "redirect:/users";
                    }
                } catch (PasswordStorage.CannotPerformOperationException e) {
                    user.setPassword("");
                } catch (PasswordStorage.InvalidHashException e) {
                    e.printStackTrace();
                }
            }

            if (bindingResult.hasErrors()) {

                model.addAttribute("user", user);
                model.addAttribute("bindingResult", bindingResult);
            }

            return "registration";
        }
        return "redirect:/login";
    }

    @RequestMapping (path = "/about")
    public String about() {
        return "about";
    }

    // TODO: 10/4/16 profile page
    @RequestMapping (path = "/profile")
    public String profile(Model model,
                          String action,
                          HttpSession httpSession) {
        if ((action != null) && (action.equals("cancel"))) {
            // reset form data & drop edits
        }

        // check for user logged in
        // add user to model

        return "/profile";
    }

    // TODO: 10/4/16 story page
    @RequestMapping(path = "/story", method = RequestMethod.GET)
    public String story(Model model, Integer animalId, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        User user = fsService.getUserOrNull((Integer)session.getAttribute("user_id"));
        Animal animal = fsService.getAnimal(animalId);
        model.addAttribute("user", user);
        model.addAttribute("animal", animal);

        return "/story";
    }

    @RequestMapping(path = "/updateStory", method = RequestMethod.POST)
    public String updateStory(Model model,
                              @Valid Animal animal,
                              BindingResult bindingResult,
                              Integer breedId,
                              HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "/story";
        } else {
            Breed breed = fsService.getBreedById(breedId);
            animal.setBreed(breed);
            User user = fsService.getUserOrNull((Integer)session.getAttribute("userId"));
            if (user == null) {
                return "redirect:/login";
            }
            animal.setUser(user);
            fsService.saveAnimal(animal);
        }
        return "/story";
    }

}
