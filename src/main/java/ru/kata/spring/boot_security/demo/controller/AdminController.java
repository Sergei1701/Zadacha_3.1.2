package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        List<User> userAll = userService.getAllUsers();
        model.addAttribute("allUsers", userAll);
        return "table";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        User user = new User();
        List roles = roleService.getAllRoles();
        model.addAttribute("newAddUser", user);
        model.addAttribute("allRoles", roles);
        return "newUser";
    }

    @PostMapping("/safeUser")
    public String safeUser(@ModelAttribute("newAddUser") User user,
                           @RequestParam("roleIds") List<Long> roleIds) {
        Set<Role> roles = new HashSet<>(roleService.findByIdRoles(roleIds));
        user.setRoles(roles);
        userService.safeUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String updateUser(@RequestParam("userId") int id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("newAddUser", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "newUser";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("userId") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
