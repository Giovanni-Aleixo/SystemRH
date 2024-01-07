package com.AppRH.AppRH.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRH.AppRH.models.Dependentes;
import com.AppRH.AppRH.models.Funcionario;
import com.AppRH.AppRH.repository.DependentesRepository;
import com.AppRH.AppRH.repository.FuncionarioRepository;

@Controller
public class FuncionarioController {

	@Autowired
	private FuncionarioRepository fr;

	@Autowired
	private DependentesRepository dr;
	
	// UTILIZADO COMO CHAMADA PARA O FORM CADASTRAR FUNCIONÁRIOS
	
	@RequestMapping(value = "/cadastrarFuncionario", method = RequestMethod.GET)
	public String form() {
		return "funcionario/formFuncionario";
	}

	// UTILIZADO PARA CADASTRAR FUNCIONÁRIOS
	
	@RequestMapping(value = "/cadastrarFuncionario", method = RequestMethod.POST)
	public String form(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/cadastrarFuncionario";
		}

		fr.save(funcionario);
		attributes.addFlashAttribute("mensagem", "Funcionário cadastrado com sucesso!");
		return "redirect:/cadastrarFuncionario";
	}

	// UTILIZADO PARA LISTAR FUNCIONÁRIOS
	
	@RequestMapping("/funcionarios")
	public ModelAndView listaFuncionarios() {
		ModelAndView mv = new ModelAndView("funcionario/listaFuncionario");
		Iterable<Funcionario> funcionarios = fr.findAll();
		mv.addObject("funcionarios", funcionarios);
		return mv;
	}

	// UTILIZADO PARA LISTAR AS DEPENDENTES
	
	@RequestMapping(value = "/dependentes/{id}", method = RequestMethod.GET)
	public ModelAndView dependentes(@PathVariable("id") long id) {
		Funcionario funcionario = fr.findById(id);
		ModelAndView mv = new ModelAndView("funcionario/dependentes");
		mv.addObject("funcionarios", funcionario);

		// LISTA DE DEPENDENTES SE BASEANDO NO FUNCIONÁRIO
		
		Iterable<Dependentes> dependentes = dr.findByFuncionario(funcionario);
		mv.addObject("dependentes", dependentes);

		return mv;

	}

	// UTILIZADO PARA ADICIONAR DEPENDENTES
	
	@RequestMapping(value="/dependentes/{id}", method = RequestMethod.POST)
	public String dependentesPost(@PathVariable("id") long id, Dependentes dependentes, BindingResult result,
			RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/dependentes/{id}";
		}
		
		if(dr.findByCpf(dependentes.getCpf()) != null) {
			attributes.addFlashAttribute("mensagem_erro", "CPF duplicado");
			return "redirect:/dependentes/{id}";
		}
		
		Funcionario funcionario = fr.findById(id);
		dependentes.setFuncionario(funcionario);
		dr.save(dependentes);
		attributes.addFlashAttribute("mensagem", "Dependente adicionado com sucesso");
		return "redirect:/dependentes/{id}";
		
	}
	
	// UTILIZADO PARA DELETAR FUNCIONÁRIOS 
	
	@RequestMapping("/deletarFuncionario")
	public String deletarFuncionario(long id) {
		Funcionario funcionario = fr.findById(id);
		fr.delete(funcionario);
		return "redirect:/funcionarios";
		
	}
	
	// MÉTODOS QUE ATUALIZAM OS FUNCIONÁRIOS
	
	@RequestMapping(value="/editar-funcionario", method = RequestMethod.GET)
	public ModelAndView editarFuncionario(long id) {
		Funcionario funcionario = fr.findById(id);
		ModelAndView mv = new ModelAndView("funcionario/update-funcionario");
		mv.addObject("funcionario", funcionario);
		return mv;
	}
	
	// REALIZA O UPDATE DE FUNCIONÁRIOS
	
	@RequestMapping(value = "/editar-funcionario", method = RequestMethod.POST)
	public String updateFuncionario(@Valid Funcionario funcionario,  BindingResult result, RedirectAttributes attributes){
		
		fr.save(funcionario);
		attributes.addFlashAttribute("successs", "Funcionário alterado com sucesso!");
		
		long idLong = funcionario.getId();
		String id = "" + idLong;
		return "redirect:/dependentes/" + id;
		
	}
	
	// UTILIZADO PARA DELETAR DEPENDENTES
	
	@RequestMapping("/deletarDependente")
	public String deletarDependente(String cpf) {
		Dependentes dependente = dr.findByCpf(cpf);
		
		Funcionario funcionario = dependente.getFuncionario();
		String codigo = "" + funcionario.getId();
		
		dr.delete(dependente);
		return "redirect:/dependentes/" + codigo;
	
	}
		
	
	
	
	
	
	
	
	

}
