package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.UserUpdateDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.UserRepository;

import java.io.IOException;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session; // request는 가방, session은 서랍

    //localhost:8080/check?username=ssar

    @GetMapping("/check")
    public ResponseEntity<String> check(String username){
        try{
            userRepository.findByUsername(username);
            return new ResponseEntity<>("중복됨", HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            return new ResponseEntity<>("사용가능 아이디입니다", HttpStatus.OK);
        }
    }

    @ResponseBody
    @GetMapping("/test/login")
    public String testLogin() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "로그인이 되지 않았습니다";
        } else {
            return "로그인 됨 : " + sessionUser.getUsername();
        }
    }

    @PostMapping("/login")
    public String login(LoginDTO loginDTO) {
        // validation check (유효성 검사)
        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }

        // 핵심 기능
        System.out.println("테스트 : username : " + loginDTO.getUsername());
        System.out.println("테스트 : password : " + loginDTO.getPassword());

        try {

            User user = userRepository.findByUsername(loginDTO.getUsername());
            System.out.println(loginDTO.getPassword());
            System.out.println(user.getPassword());
            boolean isValid = BCrypt.checkpw(loginDTO.getPassword(),user.getPassword());
            if(user != null && isValid == true ){
                session.setAttribute("sessionUser", user);
                return "redirect:/";
            }else{
                return "redirect:/exLogin";
            }
        } catch (Exception e) {
            return "redirect:/exLogin";
        }
    }

    // 실무
    @PostMapping("/join")
    public String join(JoinDTO joinDTO) {

        // validation check (유효성 검사)
        if (joinDTO.getUsername() == null || joinDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getPassword() == null || joinDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getEmail() == null || joinDTO.getEmail().isEmpty()) {
            return "redirect:/40x";
        }
        // DB에 해당 username이 있는지 체크해보기
        try{
            userRepository.findByUsername(joinDTO.getUsername());
            return "redirect:/50x";
        }catch(Exception e){
            String encPassword = BCrypt.hashpw(joinDTO.getPassword(), BCrypt.gensalt());
            System.out.println("테스트 1: "+ encPassword );
            joinDTO.setPassword(encPassword);
            userRepository.save(joinDTO); // 핵심 기능
            return "redirect:/loginForm";
        }
    }

    // 정상인
    // @PostMapping("/join")
    // public String join(String username, String password, String email) {
    // // username=ssar&password=1234&email=ssar@nate.com
    // System.out.println("username : " + username);
    // System.out.println("password : " + password);
    // System.out.println("email : " + email);

    // return "redirect:/loginForm";
    // }

    // 비정상
    // @PostMapping("/join")
    // public String join(HttpServletRequest request) throws IOException {
    // // username=ssar&password=1234&email=ssar@nate.com
    // BufferedReader br = request.getReader();

    // // 버퍼가 소비됨
    // String body = br.readLine();

    // // 버퍼에 값이 없어서, 못꺼냄.
    // String username = request.getParameter("username");

    // System.out.println("body : " + body);
    // System.out.println("username : " + username);

    // return "redirect:/loginForm";
    // }

    // 약간 정상
    // DS(컨트롤러 메서드 찾기, 바디데이터 파싱)
    // DS가 바디데이터를 파싱안하고 컨트롤러 메서드만 찾은 상황
    // @PostMapping("/join")
    // public String join(HttpServletRequest request) {
    // String username = request.getParameter("username");
    // String password = request.getParameter("password");
    // String email = request.getParameter("email");
    // System.out.println("username : " + username);
    // System.out.println("password : " + password);
    // System.out.println("email : " + email);
    // return "redirect:/loginForm";
    // }

    // ip주소 부여 : 10.5.9.200:8080 -> mtcoding.com:8080
    // localhost, 127.0.0.1
    // a태그 form태그 method=get
    @GetMapping("/joinForm")
    public String joinForm() {
        // templates/
        // .mustache
        // templates//user/joinForm.mustache
        return "user/joinForm"; // ViewResolver
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/{id}/updateForm")
    public String updateForm(@PathVariable Integer id,HttpServletRequest request) {
        User user = userRepository.findById(id);
        request.setAttribute("user",user);
        return "user/updateForm";
    }
    @PostMapping("/user/update")
    public void userUpdate(UserUpdateDTO userUpdateDTO, HttpServletResponse response) throws IOException {
        userUpdateDTO.setPassword(BCrypt.hashpw(userUpdateDTO.getPassword(), BCrypt.gensalt()));
        System.out.println(userUpdateDTO.getUsername());
        System.out.println(userUpdateDTO.getPassword());
        System.out.println(userUpdateDTO.getEmail());
        userRepository.updateByPassword(userUpdateDTO);

        response.sendRedirect("/");
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate(); // 세션 무효화 (내 서랍을 비우는 것)
        return "redirect:/";
    }
}
