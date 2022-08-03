package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UpdateUserController {
    private static final Logger logger = LoggerFactory.getLogger(UpdateUserController.class);

    @RequestMapping("/users/update")
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) {
        User user = DataBase.findUserById(request.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(
            request.getParameter("userId"),
            request.getParameter("password"),
            request.getParameter("name"),
            request.getParameter("email")
        );

        logger.debug("Update User : {}", updateUser);
        user.update(updateUser);
        return ModelAndView.withJspView("redirect:/");
    }
}
