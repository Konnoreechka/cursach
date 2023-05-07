package com.example.englishclass.Controller;

import com.example.englishclass.Entity.User;
import com.example.englishclass.Repository.ProgramRepository;
import com.example.englishclass.Repository.UserRepository;
import com.example.englishclass.UserService.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    private final ProgramRepository programRepository;

    @RequestMapping(path = {"/users", "/search"})
    public String users(Model model, String keyword) {


        if(userRepository.findAll().isEmpty()){

            return "redirect:/";
        }else if(keyword!=null){
            List<User> users = userRepository.findByKeyword(keyword);
            List<User> res = new ArrayList<>();
            users.forEach(user -> {
                if(user.getRoles().toString().equals("[USER]")){
                    res.add(user);
                };
            });
            if(res.isEmpty()){
                model.addAttribute("notfound", "Ничего не найдено");
            }
            model.addAttribute("users", res);
            return "users";
        }
        else if(!userRepository.findAll().isEmpty()) {

            Iterable<User> users = userRepository.findByRolesName("USER");
            model.addAttribute("users", users);
            return "users";
        }
        return null;
    }

    @PostMapping("/blockUser")
    public String blockUser(Model model, @RequestParam String UserEmail) {
        User user = userRepository.getUserByEmail(UserEmail);
        user.setStatus("block");
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/unblockUser")
    public String unblockUser(Model model, @RequestParam String UserEmail) {
        User user = userRepository.getUserByEmail(UserEmail);
        user.setStatus("active");
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(Model model, @RequestParam String UserEmail) {
        programRepository.deleteUsersByUserEmail(UserEmail);
        userRepository.deleteUsersByUserEmail(UserEmail);
        return "redirect:/admin/users";
    }

    @GetMapping("/personal")
    public String personal(Model model, Authentication authentication, String email){
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        User user = userService.getMemberByEmail(email);
        model.addAttribute("user", user);
        return "admin_personal";
    }

    @PostMapping("/q")
    public String loginMember(
            @RequestParam String email

    ){
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        System.out.println("Personal " + email);
        return "redirect:/personal";
    }

}
