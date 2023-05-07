package com.example.englishclass.Controller;

import com.example.englishclass.Entity.User;
import com.example.englishclass.Repository.UserRepository;
import com.example.englishclass.UserService.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class Controller {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;


    @GetMapping("/")
    public String start(){
        return "index";
    }


    @GetMapping("/signUp")
    public String signUp(){
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUpPost(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String password, Model model){

        if(userRepository.existsById(email)){
            model.addAttribute("error", "Пользователь с таким email существует");

            return "signUp";
        }else {
            if (email.endsWith("@gmail.com")||email.endsWith("@mail.ru")||email.endsWith("@yandex.ru")) {
                User user = new User(firstname, lastname, email, password);
                userService.createUser(user);
                return "redirect:/login";
            } else {
                model.addAttribute("valid", "Некорректный email. Должно содержать @gmail.com, @mail.ru, @yandex.ru");
                return "signUp";
            }


        }


    }

    @GetMapping("/booking")
    public String booking(){
        return "booking";
    }

    @GetMapping("/personal")
    public String personal(Model model, Authentication authentication, String email){
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        User user = userService.getMemberByEmail(email);
        System.out.println(user.getRoles());
        model.addAttribute("user", user);
        return "update";
    }

    @PostMapping("/q")
    public String loginMember(
            @RequestParam String email

    ){
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        System.out.println(auth.getAuthorities());

        System.out.println("Personal " + email);
        return "redirect:/personal";
    }

    @GetMapping("/update")
    public String up(String email,  Model model){
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        User user = userService.getMemberByEmail(email);
        model.addAttribute("user", user);
        return "update";
    }

    @PostMapping("/update")
    public String update(Model model, Authentication authentication, String email,
                         @RequestParam String phone,
                         @RequestParam String firstname,
                         @RequestParam String lastname
    ) throws IOException {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        User user = userService.getMemberByEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setNumber(phone);

        userService.update(user);
        model.addAttribute("user", user);

        if (user.getRoles().toString().equals("[ADMIN]")) {
            return "redirect:/admin/personal";
        } else if (user.getRoles().toString().equals("[USER]")) {
            return "redirect:/user/personal";
        }
        return null;
    }


    @GetMapping("/block")
    public String block(){
        return "block";
    }

    @GetMapping("/faq")
    public String faq(){
        return "faq";
    }
}

