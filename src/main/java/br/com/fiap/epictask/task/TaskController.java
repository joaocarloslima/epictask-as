package br.com.fiap.epictask.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService service;

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal OAuth2User user){
        model.addAttribute("avatar_url", user.getAttribute("avatar_url"));
        model.addAttribute("tasks", service.findAll());
        return "task/index";
    } 
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect){
        if (service.delete(id)){
            redirect.addFlashAttribute("success", "Tarefa apagada com sucesso");
        }else{
            redirect.addFlashAttribute("error", "Tarefa não encontrada");
        }
        return "redirect:/task";
    }

    @DeleteMapping
    public String deleteobject(Task task){
        System.out.println(task);
        return "redirect:/task";
    }

    @GetMapping("new")
    public String form(Task task){
        return "task/form";
    }

    @PostMapping
    public String save(@Valid Task task, BindingResult result, RedirectAttributes redirect){
        if (result.hasErrors()) return "/task/form";

        service.save(task);
        redirect.addFlashAttribute("success", "Tarefa cadastrada com sucesso");
        return "redirect:/task";
    }
    
}
