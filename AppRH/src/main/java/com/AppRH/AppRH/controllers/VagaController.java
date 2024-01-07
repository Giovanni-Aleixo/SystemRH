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

import com.AppRH.AppRH.models.Candidato;
import com.AppRH.AppRH.models.Vaga;
import com.AppRH.AppRH.repository.CandidatoRepository;
import com.AppRH.AppRH.repository.VagaRepository;

@Controller
public class VagaController {

	@Autowired
	private VagaRepository vr; // REPOSITORIO VAGA
	
	@Autowired
	private CandidatoRepository cr; // REPOSITORIO CANDIDATO

	// CADASTRAR VAGAS DISPONIVEIS

	@RequestMapping(value = "/cadastrarVaga", method = RequestMethod.GET)
	public String form() {
		return "vaga/formVaga";
	}

	@RequestMapping(value = "/cadastrarVaga", method = RequestMethod.POST)
	public String form(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos...");
			return "redirect:/cadastrarVaga";
		}

		vr.save(vaga);
		attributes.addFlashAttribute("mensagem", "Candidatura realizada com sucesso!");
		return "redirect:/cadastrarVaga";

	}

	// LISTA DE VAGAS

	@RequestMapping("/vagas")
	public ModelAndView listaVagas() {
		ModelAndView mv = new ModelAndView("vaga/listaVaga");
		Iterable<Vaga> vagas = vr.findAll();
		mv.addObject("vagas", vagas);
		return mv;

	}

	//

	@RequestMapping(value = "/vaga/{codigo}", method = RequestMethod.GET)
	public ModelAndView detalhesVaga(@PathVariable("codigo") long codigo) {
		Vaga vaga = vr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("vaga/detalhesVaga");
		mv.addObject("vaga", vaga);

		Iterable<Candidato> candidatos = cr.findByVaga(vaga);
		mv.addObject("candidatos", candidatos);

		return mv;

	}

	// MÉTODO PARA DELETAR VAGAS

	@RequestMapping("/deletarVaga")
	public String deletarvaga(long codigo) {
		Vaga vaga = vr.findByCodigo(codigo);
		vr.delete(vaga);
		return "redirect:/vagas";

	}
	
	// ADICIONAR CANDIDATO

	@RequestMapping(value = "/vaga/{codigo}", method = RequestMethod.POST)
	public String detalhesVagaPost(@PathVariable("codigo") long codigo, @Valid Candidato candidato,
			BindingResult result, RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			attributes.addAttribute("mensagem", "Verifique os campos");
			return "redirect:/vaga/{codigo}";
		}
		
		// RG DUPLICADO
		if(cr.findByRg(candidato.getRg()) != null) {
			attributes.addFlashAttribute("mensagem_erro", "RG duplicado");
			return "redirect:/vaga/{codigo}";
		}
		
		Vaga vaga = vr.findByCodigo(codigo);
		candidato.setVaga(vaga);
		cr.save(candidato);
		attributes.addFlashAttribute("mensagem", "Candidato adicionado com sucesso!");
		return "redirect:/vaga/{codigo}";
		

	}
	
	// MÉTODO PARA DELETAR CANDIDATOS PELO RG
	
	@RequestMapping("/deletarCandidato")
	public String deletarCandidato(String rg) {
		Candidato candidato = cr.findByRg(rg);
		Vaga vaga = candidato.getVaga();
		String codigo = "" + vaga.getCodigo();	
		cr.delete(candidato);
		return "redirect:/" + codigo;
				
	}
	
	// MÉTODO PARA ATUALIZAR VAGAS
	// FORMULÁRIO PARA EDIÇÃO DE VAGA
	
	@RequestMapping(value="/editar-vaga", method = RequestMethod.GET)
	public ModelAndView editarVaga(long codigo ) {
		Vaga vaga = vr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("vaga/update-vaga");
		mv.addObject("vaga", vaga);
		return mv;
	}
	
	//MÉTODO PARA UPDATE DA VAGA
	
	@RequestMapping(value = "/editar-vaga", method = RequestMethod.POST)
	public String updateVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {
		vr.save(vaga);
		attributes.addFlashAttribute("success", "Vaga alterada com sucesso!");
		long codigoLong = vaga.getCodigo();
		String codigo = "" +codigoLong;
		return "redirect:/" + codigo;
		
	}
	

}
