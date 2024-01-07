package com.AppRH.AppRH.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.AppRH.AppRH.models.Dependentes;
import com.AppRH.AppRH.models.Funcionario;
import com.AppRH.AppRH.models.Vaga;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.AppRH.AppRH.models.Dependentes;
import com.AppRH.AppRH.models.Funcionario;

public interface DependentesRepository extends CrudRepository<Dependentes, Long> {

	Iterable<Dependentes> findByFuncionario(Funcionario funcionario);
	
	// pensando no método delete
	Dependentes findByCpf(String cpf);
	Dependentes findById(long id);
	
	// criado para implementar
	List<Dependentes> findByNome(String nome);
	
	@Query(value = "select u from Dependentes u where u.nome like %?1%")
	List<Dependentes> findByNomesDependentes(String nome);

	
}