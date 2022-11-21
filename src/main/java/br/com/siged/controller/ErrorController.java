package br.com.siged.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/errors/**")
public class ErrorController {
		
	
	@RequestMapping(value = "erro", method = RequestMethod.GET)
    public ModelAndView renderErrorPage(HttpServletRequest request) {		
		
		ModelAndView errorPage = new ModelAndView("errors/erro");
        String errorMsg = request.getParameter("mensagem");        
        errorPage.addObject("errorMsg", errorMsg);
        return errorPage;
        
    }
	

}
