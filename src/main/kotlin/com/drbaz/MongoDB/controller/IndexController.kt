package com.drbaz.MongoDB.controller

import com.drbaz.MongoDB.repository.BankRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class IndexController (private val bankRepository: BankRepository) {

	@RequestMapping("/")
		fun index(): String {
		return "index"
	}
	@RequestMapping("/home")
		fun home(model: Model): String{
        model.addAttribute("banks",bankRepository.findAll())
		return "home"
	}
}