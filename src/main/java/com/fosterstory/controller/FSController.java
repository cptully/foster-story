package com.fosterstory.controller;

import com.fosterstory.bean.Login;
import com.fosterstory.bean.Search;
import com.fosterstory.entity.*;
import com.fosterstory.service.*;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by chris on 10/3/16.
 */
@Controller
public class FSController {

    private final Integer ADMIN = 1;
    private final Integer USER = 2;

    @Autowired
    FSService fsService;

    @Autowired
    UserService userService;

    @Autowired
    TumblrService tumblrService;

    @Autowired
    AnimalService animalService;

    @Autowired
    PostService postService;

    @Autowired
    ImageService imageService;

    //@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("list");
        registry.addViewController("/about").setViewName("about");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/profile").setViewName("profile");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/story").setViewName("story");
        registry.addViewController("/viewStory").setViewName("viewStory");
    }


    @RequestMapping(path = "/")
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
        return "list";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register(User user,
                           Model model,
                           HttpSession session) {
        model.addAttribute("user", user);
        return "register";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
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
                    FieldError fieldError = new FieldError("user", "confirmPassword", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[]) null, "Passwords do not match");
                    bindingResult.addError(fieldError);
                }
            } catch (PasswordStorage.CannotPerformOperationException e) {
                FieldError fieldError = new FieldError("user", "password", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[]) null, "Invalid password");
                bindingResult.addError(fieldError);
                e.printStackTrace();
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("bindingResult", bindingResult);

        return "register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Login login, String returnPath, Model model, HttpSession session) {

        model.addAttribute("login", login);
        model.addAttribute("returnPath", returnPath);

        // get the user (or null if not logged in)
        model.addAttribute("user", fsService.getUserOrNull((Integer) session.getAttribute("userId")));

        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model, @Valid Login login, HttpSession session) {

        User user = fsService.authenticateUser(login);

        // get the user (or null if not logged in)
        model.addAttribute("user", user);

        if (user != null) {
            session.setAttribute("userId", user.getId());
            if (login.getReturnPath() != null) {
                return "redirect:" + login.returnPath;
            } else {
                return "redirect:/";
            }
        } else {
            return "login";
        }
    }

    @RequestMapping(path = "/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


    @RequestMapping(path = "/about")
    public String about() {
        return "about";
    }

    // TODO: 10/4/16 profile page
    @RequestMapping(path = "/profile")
    public String profile(Model model,
                          String action,
                          @Valid User userData,
                          BindingResult bindingResult,
                          HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
//            model.addAttribute("returnPath", "/profile");
            return "redirect:/login?returnPath=/profile";
        }

        User user = userData;
        if (!bindingResult.hasErrors()) {
            user = fsService.getUser(userId);
            if ((action != null) && (action.equals("save"))) {
                // update the user data from the form with the logged in user id & password
                // and keep the password out of the UI
                if (userData.getPassword() == null) {
                    userData.setPassword(user.getPassword());
                }

                if (action.equals("save")) {
                    // save the user and pray
                    try {
                        user = fsService.saveUser(userData);
                    } catch (PasswordStorage.CannotPerformOperationException e) {
                        //error
                        FieldError fieldError = new FieldError("user", "password", user.getPassword(), false,
                                new String[]{"Invalid.user.password"},
                                (String[]) null, "Username / password combination incorrect");
                        bindingResult.addError(fieldError);

                        e.printStackTrace();
                        return "redirect:/login";
                    }
                }
            }
        }

        if (bindingResult.hasErrors()) {

        }

        model.addAttribute("user", user);
        return "profile";
    }

    @RequestMapping(path = "/story", method = RequestMethod.GET)
    public String story(Model model,
                        @Valid Animal animal,
                        BindingResult bindingResult,
                        HttpSession session) {
        if ((session.getAttribute("userId") == null) || (fsService.getUserOrNull((Integer) session.getAttribute("userId")) == null)) {
            return "redirect:/login?returnPath=/story";
        }

        if (bindingResult.hasErrors()) {
            return "story";
        }
        User user = fsService.getUser((Integer) session.getAttribute("userId"));
        model.addAttribute("user", user);
        List<Animal> animals = user.getAnimals();
        model.addAttribute("animals", animals);
        model.addAttribute("count", animals.size());
        if (animals.size() > 0) {
            animal = animals.get(animals.size() - 1);
        }
        model.addAttribute("animal", animal);
        model.addAttribute("types", fsService.listTypes());
        model.addAttribute("breeds", fsService.listBreeds());
        return "story";
    }

    @RequestMapping(path = "/story", method = RequestMethod.POST)
    public String story(Model model,
                        @Valid Animal animal,
                        BindingResult bindingResult,
                        @RequestParam(name = "file") MultipartFile file,
                        String action,
                        HttpSession session) {
        if ((session.getAttribute("userId") == null) || (fsService.getUserOrNull((Integer) session.getAttribute("userId")) == null)) {
            return "redirect:/login?returnPath=/story";
        }

        if (bindingResult.hasErrors()) {
            return "story";
        }
        User user = fsService.getUser((Integer) session.getAttribute("userId"));
        List<Animal> animals = user.getAnimals();
        model.addAttribute("animals", animals);
        model.addAttribute("count", animals.size());
        model.addAttribute("user", user);
        model.addAttribute("types", fsService.listTypes());
        model.addAttribute("breeds", fsService.listBreeds());

        if ((action != null) && (action.equals("save"))) {
            try {
                if (animal != null) {
                    animal.setUser(user);
                    if (animal.getBreed() == null) {
                        Breed breed = fsService.getBreedById(animal.getBreed().getId());
                        animal.setBreed(breed);
                    }

                    if (!file.isEmpty()) {
                        try {
                            Image image = new Image();
                            image.setImage(file.getBytes());
                            image.setContentType(file.getContentType());
                            animal.getImages().add(image);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!user.getAnimals().contains(animal)) {
                        user.getAnimals().add(animal);
                        fsService.saveUser(user);
                    } else {
                        fsService.saveAnimal(animal);
                    }
                }
                model.addAttribute("animal", animal);
                return "redirect:/story";
            } catch (PasswordStorage.CannotPerformOperationException e) {
                // set errors
                return "story";
            }
        } else if ((action != null) && (action.equals("clear"))) {
            animal = new Animal();
        } else {
            if (animals.size() >= 1) {
                animal = animals.get(animals.size() - 1);
            } else {
                animal = new Animal();
            }
        }

        model.addAttribute("animal", animal);
        return "story";
    }

    @RequestMapping(path = "/viewStory")
    public String viewStory(Model model,
                            Integer animalId,
                            Integer photoId,
                            Pageable pageable) {
        Animal animal;
        if (animalId != null) {
            animal = fsService.getAnimal(animalId);
        } else {
            animal = new Animal();
        }
//        if (!animal.getTumblr().equals("")) {
//            tumblrService.getPostsFromTumblr(animal.getTumblr());
//        } else if (!animal.getUser().getTumblr().equals("")) {
//            tumblrService.getPostsFromTumblr(animal.getUser().getTumblr());
//        } else {
//        tumblrService.getPostsFromTumblr("suncities4paws");
        tumblrService.getPostsFromTumblr("");
//        }

//        List<Post> posts = animalService.getPostsByAnimalId(animalId);
        Page<Post> posts = postService.listPosts(pageable);
        model.addAttribute("posts", posts);
        return "viewStory";
    }
}