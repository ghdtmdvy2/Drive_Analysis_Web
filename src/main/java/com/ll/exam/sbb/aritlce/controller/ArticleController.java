package com.ll.exam.sbb.aritlce.controller;

import com.ll.exam.sbb.answer.dto.AnswerForm;
import com.ll.exam.sbb.aritlce.dto.ArticleForm;
import com.ll.exam.sbb.aritlce.entity.Article;
import com.ll.exam.sbb.aritlce.service.ArticleService;
import com.ll.exam.sbb.user.entity.SiteUser;
import com.ll.exam.sbb.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/article")
@Controller
@RequiredArgsConstructor
public class ArticleController {
    // @Autowired // νλ μ£Όμ
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "") String kw, @RequestParam(defaultValue = "") String sortCode, Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Article> paging = articleService.getList(kw, page, sortCode);

        model.addAttribute("paging", paging);

        return "article/article_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable long id, AnswerForm answerForm) {
        Article article = articleService.getArticle(id);

        model.addAttribute("article", article);

        return "article/article_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String articleModify(ArticleForm articleForm, @PathVariable("id") Integer id, Principal principal) {
        Article article = this.articleService.getArticle(id);

        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "μμ κΆνμ΄ μμ΅λλ€.");
        }

        articleForm.setSubject(article.getSubject());
        articleForm.setContent(article.getContent());

        return "article/article_form";
    }

    @PostMapping("/modify/{id}")
    public String articleModify(@Valid ArticleForm articleForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "article/article_form";
        }

        Article article = this.articleService.getArticle(id);

        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "μμ κΆνμ΄ μμ΅λλ€.");
        }
        this.articleService.modify(article, articleForm.getSubject(), articleForm.getContent());
        return String.format("redirect:/article/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String articleCreate(ArticleForm articleForm) {
        return "article/article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String articleCreate(Principal principal, Model model, @Valid ArticleForm articleForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "article/article_form";
        }

        SiteUser siteUser = userService.getUser(principal.getName());

        articleService.create(articleForm.getSubject(), articleForm.getContent(), siteUser);
        return "redirect:/article/list"; // μ§λ¬Έ μ μ₯ν μ§λ¬Έλͺ©λ‘μΌλ‘ μ΄λ
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String articleDelete(Principal principal, @PathVariable("id") Integer id) {
        Article article = articleService.getArticle(id);

        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "μ­μ κΆνμ΄ μμ΅λλ€.");
        }

        articleService.delete(article);

        return "redirect:/article/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String articleVote(Principal principal, @PathVariable("id") Long id) {
        Article article = articleService.getArticle(id);
        SiteUser siteUser = userService.getUser(principal.getName());

        articleService.vote(article, siteUser);
        return "redirect:/article/detail/%d".formatted(id);
    }
}
