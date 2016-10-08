package com.fosterstory.controller;

import com.fosterstory.bean.Login;
import com.fosterstory.bean.Search;
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
                          @Valid User user,
                          BindingResult bindingResult,
                          HttpSession session) {
        Integer userId = (Integer)session.getAttribute("userId");
        if (userId  == null) {
            return "redirect:/login";
        }

        if (!bindingResult.hasErrors()) {
            if ((action != null) && (action.equals("cancel"))) {
                // reset form data & drop edits
                user = fsService.getUser(userId);
            } else if ((action != null) && (action.equals("save"))) {
                if (user.getId() == null) {user.setId(userId);}
                try {
                    fsService.saveUser(user);
                } catch (PasswordStorage.CannotPerformOperationException e) {
                    //error
//                    FieldError fieldError = new FieldError("user", "password", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[]) null, "Username / password combination incorrect");
//                    bindingResult.addError(fieldError);

                    e.printStackTrace();
                }
            }

        }


//        user = fsService.getUser(userId);
        model.addAttribute("user", user);
        return "/profile";
    }

    // TODO: 10/4/16 story page
    @RequestMapping(path = "/story", method = RequestMethod.GET)
    public String story(Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        User user = fsService.getUserOrNull((Integer)session.getAttribute("userId"));
        List<Animal> animals = user.getAnimals();
        model.addAttribute("animals", animals);
        model.addAttribute("count", animals.size());
        model.addAttribute("user", user);

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
