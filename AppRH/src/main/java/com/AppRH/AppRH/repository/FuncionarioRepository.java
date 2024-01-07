package com.AppRH.AppRH.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

import com.AppRH.AppRH.models.Funcionario;

import java.util.List;

public interface FuncionarioRepository extends CrudRepository<Funcionario, Long> {
	
	Funcionario findById(long id);
	
	Funcionario findByNome(String nome);
	
	@Query(value = "select u from Funcionario u where u.nome like %?1%")    // UTILIZADO PARA REALIZAR A BUSCA
	List<Funcionario>findByNomes(String nome);

}
