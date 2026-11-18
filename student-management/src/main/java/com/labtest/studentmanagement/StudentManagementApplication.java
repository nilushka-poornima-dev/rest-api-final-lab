package com.labtest.studentmanagement;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ─── Entry Point ────────────────────────────────────────────────────────────
@SpringBootApplication
public class StudentManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
    }
}

// ─── Entity ─────────────────────────────────────────────────────────────────
@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String course;
    private int age;
}

// ─── Repository ─────────────────────────────────────────────────────────────
interface StudentRepository extends JpaRepository<Student, Long> {}

// ─── Controller ─────────────────────────────────────────────────────────────
@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
class StudentController {

    private final StudentRepository repo;

    // List all students
    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", repo.findAll());
        return "students/list";
    }

    // Show create form
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("isEdit", false);
        return "students/form";
    }

    // Save new student
    @PostMapping
    public String save(@ModelAttribute Student student) {
        repo.save(student);
        return "redirect:/students";
    }

    // Show edit form
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", repo.findById(id).orElseThrow());
        model.addAttribute("isEdit", true);
        return "students/form";
    }

    // Update student
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Student student) {
        student.setId(id);
        repo.save(student);
        return "redirect:/students";
    }

    // Delete student
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/students";
    }
}

// ─── Home Redirect ───────────────────────────────────────────────────────────
@Controller
class HomeController {
    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }
}
