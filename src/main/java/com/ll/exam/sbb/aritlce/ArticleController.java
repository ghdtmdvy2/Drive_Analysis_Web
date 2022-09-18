package com.ll.exam.sbb.aritlce;

import com.ll.exam.sbb.answer.AnswerForm;
import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.question.QuestionForm;
import com.ll.exam.sbb.user.SiteUser;
import com.ll.exam.sbb.user.UserService;
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
    // @Autowired // 필드 주입
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/list")
    // 이 자리에 @ResponseBody가 없으면 resources/question_list/question_list.html 파일을 뷰로 삼는다.
    public String list(@RequestParam(defaultValue = "") String kw, @RequestParam(defaultValue = "") String sortCode, Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Article> paging = articleService.getList(kw, page, sortCode);

        // 미래에 실행된 question_list.html 에서
        // questionList 라는 이름으로 questionList 변수를 사용할 수 있다.
        model.addAttribute("paging", paging);

        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable long id, AnswerForm answerForm) {
        Article article = articleService.getArticle(id);

        model.addAttribute("question", article);

        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(ArticleForm articleForm, @PathVariable("id") Integer id, Principal principal) {
        Article article = this.articleService.getArticle(id);

        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        articleForm.setSubject(article.getSubject());
        articleForm.setContent(article.getContent());

        return "question_form";
    }

    @PostMapping("/modify/{id}")
    public String questionModify(@Valid ArticleForm articleForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        Article article = this.articleService.getArticle(id);

        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.articleService.modify(article, articleForm.getSubject(), articleForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(ArticleForm articleForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(Principal principal, Model model, @Valid ArticleForm articleForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        SiteUser siteUser = userService.getUser(principal.getName());

        articleService.create(articleForm.getSubject(), articleForm.getContent(), siteUser);
        return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Article article = articleService.getArticle(id);

        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        articleService.delete(article);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Long id) {
        Article article = articleService.getArticle(id);
        SiteUser siteUser = userService.getUser(principal.getName());

        articleService.vote(article, siteUser);
        return "redirect:/question/detail/%d".formatted(id);
    }
}
