package com.fosterstory.controller;

import com.fosterstory.bean.Login;
import com.fosterstory.bean.Search;
import com.fosterstory.entity.Address;
import com.fosterstory.entity.Animal;
import com.fosterstory.entity.Breed;
import com.fosterstory.entity.User;
import com.fosterstory.service.FSService;
import com.fosterstory.utility.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

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
                if (user.getPassword().equals(user.getConfirmPassword())) {
                    fsService.saveUser(user);
                    session.setAttribute("userId", user.getId());
                    return "redirect:/";
                } else {
                    FieldError fieldError = new FieldError("user", "confirmPassword", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[])null, "Passwords do not match");
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


    @RequestMapping (path = "/about")
    public String about() {
        return "about";
    }

    // TODO: 10/4/16 profile page
    @RequestMapping (path = "/profile")
    public String profile(Model model,
                          String action,
                          @Valid User userData,
                          @Valid Address address,
                          BindingResult bindingResult,
                          HttpSession session) {
        Integer userId = (Integer)session.getAttribute("userId");
        if (userId  == null) {
            return "redirect:/login";
        }

        User user = userData;
        if (!bindingResult.hasErrors()) {
            user = fsService.getUser(userId);
            if ((action != null) && (action.equals("save"))) {
                // update the user data from the form with the logged in user id & password
                if (userData.getId() == null) {userData.setId(userId);}
                if (userData.getPassword() == null) {userData.setPassword(user.getPassword());}

                // the form will not populate the address property
                // Setting the address id from the saved user data
                address.setId(user.getAddress().getId());

                // overwrite the null user on the user data from the from
                // with address data recovered in seperate object
                userData.setAddress(address);

                if (action.equals("save")) {
                    // save the user and pray
                    try {
                        user = fsService.saveUser(userData);
                    } catch (PasswordStorage.CannotPerformOperationException e) {
                        //error
//                        FieldError fieldError = new FieldError("user", "password", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[]) null, "Username / password combination incorrect");
//                        bindingResult.addError(fieldError);

                        e.printStackTrace();
                    }
                }
            }
        }

        address = user.getAddress();
        model.addAttribute("user", user);
        model.addAttribute("address", address);
        return "/profile";
    }

    // TODO: 10/4/16 story page
    @RequestMapping(path = "/story")
    public String story(Model model,
                        @Valid Animal animal,
                        BindingResult bindingResult,
                        String action,
                        HttpSession session) {
        if ((session.getAttribute("userId") == null) || (fsService.getUserOrNull((Integer)session.getAttribute("userId")) == null)){
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "/story";
        }
        User user = fsService.getUser((Integer) session.getAttribute("userId"));

        if ((action != null) && (action.equals("save"))) {
            try {
                if (animal != null) {
                    animal.setUser(user);
                    if (animal.getBreed() != null) {
                        Breed breed = fsService.getBreedById(animal.getBreed().getId());
                        animal.setBreed(breed);
                    }

                    if (!user.getAnimals().contains(animal)) { user.getAnimals().add(animal); }

                    fsService.saveUser(user);
//                    fsService.saveAnimal(animal);
                }
            } catch (PasswordStorage.CannotPerformOperationException e) {
                // set errors
                return "/story";
            }
        }

        List<Animal> animals = user.getAnimals();
        model.addAttribute("animals", animals);
        model.addAttribute("count", animals.size());
        model.addAttribute("user", user);
        model.addAttribute("types", fsService.listTypes());
        model.addAttribute("breeds", fsService.listBreeds());

        return "/story";
    }

}
