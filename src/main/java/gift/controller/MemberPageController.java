package gift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberPageController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "member/register";
    }

    @GetMapping("/myinfo")
    public String myInfoPage() {
        return "member/myinfo";
    }

    @GetMapping("/wishes")
    public String wishListPage() {
        return "member/wishlist";
    }
}
