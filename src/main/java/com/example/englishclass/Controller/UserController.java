package com.example.englishclass.Controller;

import com.example.englishclass.Entity.Program;
import com.example.englishclass.Entity.ProgramDescription;
import com.example.englishclass.Entity.User;
import com.example.englishclass.Repository.ProgramDescriptionRepository;
import com.example.englishclass.Repository.ProgramRepository;
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

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    private final ProgramDescriptionRepository programDescriptionRepository;

    private final ProgramRepository programRepository;




    @GetMapping("/services")
    public String services(Model model){
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Iterable<Program> programs = programRepository.getAllByUserEmail(email);
        model.addAttribute("services", programs);
        return "services";
    }

    @PostMapping("/reject")
    public String reject(@RequestParam Long id){
        programRepository.deleteById(id);
        return "redirect:/user/services";
    }

    @GetMapping("/personal")
    public String personal(Model model, Authentication authentication, String email) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        User user = userService.getMemberByEmail(email);
        model.addAttribute("user", user);

        Iterable<Program> programs = programRepository.getAllByUserEmail(email);
        model.addAttribute("programs", programs);
        model.addAttribute("email", email);
        return "personal";
    }

    @PostMapping("/q")
    public String loginMember(
            @RequestParam String email

    ) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        System.out.println("Personal " + email);
        return "redirect:/personal";
    }

    @GetMapping("/course")
    public String course(Model model) {
        Iterable<ProgramDescription> programs = programDescriptionRepository.findAll();
        model.addAttribute("programs", programs);

        return "Course";
    }

    @PostMapping("/course")
    public String coursePost(Model model,
                             @RequestParam String program,@RequestParam String date,
                             String email) {
        ProgramDescription programDescription = programDescriptionRepository.getProgramDescriptionByProgram(program);//depositDes
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        User user = userService.getMemberByEmail(email);

        Program program1 = new Program();
        program1.setProgram(program);
        program1.setProgramTerm(date);
        program1.setPrice(programDescription.getPrice());
        program1.setUserEmail(email);


        programRepository.save(program1);

        return "redirect:/user/personal";
    }

}
