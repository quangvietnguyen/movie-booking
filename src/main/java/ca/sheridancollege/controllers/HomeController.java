package ca.sheridancollege.controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.beans.Order;
import ca.sheridancollege.beans.User;
import ca.sheridancollege.database.DataBaseAccess;

@Controller
public class HomeController {
	
	@Autowired
	@Lazy
	private DataBaseAccess da;
	
	@GetMapping("/")
	public String goHome() {
		return "index.html";
	}
	
	@GetMapping("/admin")
	public String goHomeUser() {
		return "/admin/index.html";
	}
	
	@GetMapping("/login")
	public String goLoginPage() {
		return "login.html";
	}
	
	@GetMapping("/register")
	public String goRegistration() {
		return "registration.html";
	}
	
	@PostMapping("/register")
	public String processRegistration(@RequestParam String name, @RequestParam String password, @RequestParam int role, HttpSession session) {
		da.addUser(name,password);
		//model.addAttribute("user", user);
		long userId = da.findUserAccount(name).getUserId();
		//int role = user.getRole();
		da.addRole(userId, role);
		if (role == 2) session.setAttribute("price", 12);
		else if (role == 3) session.setAttribute("price", 8);
		else if (role == 4) session.setAttribute("price", 6.4);
		else if (role == 5) session.setAttribute("price", 4);
		else if (session.getAttribute("counter") == null) session.setAttribute("price", 15);
		//long price = da.getPricesById(role);
		//session.setAttribute("price", price);
		return "redirect:/";
	}
	
	@GetMapping("/book")
	public String bookMovie(Model model,@ModelAttribute User user, HttpSession session) {
		session.setAttribute("Id", session.getId());
		session.setAttribute("userId", new User());
		model.addAttribute("order", new Order());
		return "book.html";
	}
	
	@PostMapping("/booked")
	public String bookedMovie(Model model, @ModelAttribute Order order, HttpSession session) throws IOException {
		session.getAttribute("Id");
		//User user = (User)session.getAttribute("userId");
		String movieName = order.getMovieName();
		String movieDate = order.getMovieDate();
		String movieTime = order.getMovieTime();
		int movieSeat = order.getMovieSeat();
		PrintWriter output = new PrintWriter(new File("src/main/resources/static/ticket.txt"));
		if(da.findMovieOrdered(movieName,movieDate,movieTime,movieSeat)) {
			da.addOrder(movieName,movieDate,movieTime,movieSeat);
			output.println("Movie Ticket");
			output.println("-------------------");
			output.println("Movie Name:");
			output.println(movieName);
			output.println("Date");
			output.println(movieDate);
			output.println("Time");
			output.println(movieTime);
			output.println("Seat");
			output.println(movieSeat);
			output.close();
			return "ticket.html";
		} else {
			output.close();
			return "noticket.html";
		}
	}
	
	@GetMapping("/add")
	public String addRow(@RequestParam String name, @RequestParam String date, @RequestParam String time, @RequestParam int seat, HttpSession session) {
		session.getId();
		da.addOrder(name, date, time, seat);
		return "result.html";
	}
	
	@GetMapping("/delete")
	public String deleteOrder(@RequestParam int orderId, HttpSession session) {
		session.getId();
		da.deleteOrder(orderId);
		return "result.html";
	}
	
	@GetMapping("/view")
	public String viewTable(HttpSession session, Model model) {
		session.getId();
		model.addAttribute("table", da.viewTable(1));
		return "table.html";
	}
}

